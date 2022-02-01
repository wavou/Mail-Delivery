import React, {useEffect, useRef, useState} from 'react';
import { Toast } from 'primereact/toast';
import { FileUpload } from 'primereact/fileupload';

import { Tooltip } from 'primereact/tooltip';
import axios from "axios";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {
    CREATE_CONTACT_URL,
    CREATE_CONTACTS_URL,
    EMPTY_CONTACT,
    GET_ALL_CONTACTS_URL,
    RESPONSE_OK,
    SEND_EMAIL_URL,
    UPDATE_CONTACT_URL
} from "../constants/constants";
import {InputTextarea} from "primereact/inputtextarea";
import {Button} from "primereact/button";
import { classNames } from 'primereact/utils';
import CreateOrUpdateContact from "./CreateOrUpdateContact";
import {Toolbar} from "primereact/toolbar";
import {convertSecondsToTime} from "../utils/DateConversionUtil";


export const Contacts = () => {


    const toast = useRef(null);

    const [selectedContact, setSelectedContact] = useState(EMPTY_CONTACT);
    const [contacts, setContacts] = useState([]);
    const [selectedContacts, setSelectedContacts] = useState(null);
    const [contactDialog, setContactDialog] = useState(false);
    const [mailBody, setMailBody] = useState('');

    const  openNew = () => {
        setSelectedContact(EMPTY_CONTACT);
        setContactDialog(true);
    }

    useEffect(() => {
        getAllContacts();
    }, []);

    const onUpload = (e) => {
        toast.current.show({severity: 'info', summary: 'Success', detail: 'File Uploaded'});
    }

    const uploadHandler = ({files}) => {
        const [file] = files;
        const fileReader = new FileReader();
        const contactsFromText = [];

       fileReader.onload = (e) => {
           const file = e.target.result;
           const allLines = file.split(/\r\n|\n/);

           allLines.forEach( (line) => {
               contactsFromText.push(parseLineAndCreateContact(line));
           });
           sendCreateContactsRequest(contactsFromText);
        }
        fileReader.readAsText(file);
    };

    const parseLineAndCreateContact = (line) => {
        const name = line.substr(0, line.indexOf('<')).trim();
        let email = line.split('<')[1];
        email = email.substr(0, email.indexOf('>'));

        return  {
            name: name,
            email: email,
            inCampaign: false,
            isEmailSent: false,
            clickedLink: false,
            durationUntilClick: 0
        };

    }

    const getAllContacts = () => {

        axios.get(GET_ALL_CONTACTS_URL).then(res => {
            if (res.status === RESPONSE_OK) {
                setContacts(res.data);
            }
        });

    }

    const sendCreateContactsRequest = (contacts) => {
        axios.post(CREATE_CONTACTS_URL, contacts).then(res => {
            setContacts(res.data);
        });
    }

    const sendCreateContactRequest = (contact) => {
        axios.post(CREATE_CONTACT_URL, contact).then(res => {
            setContacts(contacts => [...contacts, res.data]);
        });
    }


    const sendUpdateContactRequest = (contact) => {
        axios.put(UPDATE_CONTACT_URL, contact).then(res => {
            const index = contacts.findIndex(contact => contact.id === res.data.id);
            const tempArray = [...contacts];
            tempArray[index] = res.data;
            setContacts(tempArray);
        });
    }

    const sendEmailRequest = (mailString) => {
        axios.post(SEND_EMAIL_URL, mailString).then(res => {
            toast.current.show({severity: 'success', summary: 'Success Message', detail: res.data});
        }).catch(res => {
            toast.current.show({severity: 'error', summary: 'Error Message', detail: res.response.data});
        })
    }

    const  onSendEmail = () => {
        if(selectedContacts && (mailBody !== '')){
            const contactEmails = [];
            selectedContacts.forEach(contact => {
                if(!contact.isEmailSent)
                    contactEmails.push(contact.email);
            });

            if(contactEmails.length){
                const mailToContacts = {
                    mailBody: mailBody,
                    contactEmails: contactEmails
                }
                sendEmailRequest(mailToContacts);
            }

        }

    }


    const mailBodyTemplate = (rowData) => {
        return <i className={classNames('pi', {'true-icon pi-check-circle'
                : rowData.emailSent, 'false-icon pi-times-circle'
                : !rowData.emailSent})}/>;
    }

    const linkBodyTemplate = (rowData) => {
        return <i className={classNames('pi', {'true-icon pi-check-circle'
                : rowData.clickedLink, 'false-icon pi-times-circle'
                : !rowData.clickedLink})}/>;
    }

    const durationBodyTemplate = (rowData) => {
      return convertSecondsToTime(rowData.durationUntilClick);
    }


    const leftToolbarTemplate = () => {
        return (
            <React.Fragment>
                <Button label="New" icon="pi pi-plus" className="p-button-success mr-2" onClick={openNew} />
            </React.Fragment>
        )
    }

    const editContact = (contact) => {
        setSelectedContact({...contact});
        setContactDialog(true);
    }

    const actionBodyTemplate = (rowData) => {
        return (
            <React.Fragment>
                <Button icon="pi pi-pencil" className="p-button-rounded p-button-success mr-2" onClick={() => editContact(rowData)} />
            </React.Fragment>
        );
    }


    return (

        <div>
            <CreateOrUpdateContact contactDialog = {contactDialog}
                                   hideDialog = { () => setContactDialog(false)}
                                   contacts = {contacts}
                                   selectedContact = {selectedContact}
                                   create = {sendCreateContactRequest}
                                   update = {sendUpdateContactRequest} />

            <Toast ref={toast}/>

            <Toolbar className="mb-4" left={leftToolbarTemplate} />

            <DataTable className="p-datatable-sm" scrollable
                       value={contacts}
                       editMode="row"
                       dataKey="id"
                       selection={selectedContacts}
                       onSelectionChange={e => setSelectedContacts(e.value)}
                       selectionMode={"multiple"}
            >
                <Column selectionMode="multiple" headerStyle={{ width: '3em' } } />
                <Column field="name" header="Name"/>
                <Column field="email" header="E Mail"/>
                <Column field="emailSent" header="E Mail Sent" dataType = "boolean" bodyClassName="text-center"
                        style={{ minWidth: '8rem' }} body={ mailBodyTemplate} />
                <Column field="clickedLink" header="Link Has Clicked" dataType = "boolean" bodyClassName="text-center"
                        style={{ minWidth: '8rem' }} body={linkBodyTemplate} />
                <Column field="durationUntilClick" body = {durationBodyTemplate} header="Duration Until Click" />
                <Column body={actionBodyTemplate} exportable={false} style={{ minWidth: '8rem' }}></Column>

            </DataTable>

            <h5>E Mail </h5>
            <InputTextarea value={mailBody} onChange={(e) => setMailBody(e.target.value)} rows={10} cols={60} />
            <Button label="Submit" icon="pi pi-check"  onClick={ onSendEmail} style = {{margin : '5px' }}/>

            <Tooltip target=".custom-choose-btn" content="Choose" position="bottom" />
            <Tooltip target=".custom-upload-btn" content="Upload" position="bottom" />
            <Tooltip target=".custom-cancel-btn" content="Clear" position="bottom" />

            <div className="card">
                <h5>Upload Contacts File</h5>
                <FileUpload mode="basic" name="demo[]" customUpload={true} accept="text/*" maxFileSize={1000000}
                            multiple={false}  onUpload={onUpload} uploadHandler={uploadHandler} />

            </div>
        </div>
    )
}
