import React, {useEffect, useRef, useState} from 'react';
import { Toast } from 'primereact/toast';
import { FileUpload } from 'primereact/fileupload';

import { Tooltip } from 'primereact/tooltip';
import axios from "axios";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {CREATE_CONTACT_URL, GET_ALL_CONTACTS_URL, RESPONSE_OK, SEND_EMAIL_URL} from "../constants/constants";
import {InputTextarea} from "primereact/inputtextarea";
import {Button} from "primereact/button";


export const FileUploadDemo = () => {

    const toast = useRef(null);

    const [contacts, setContacts] = useState([]);
    const [selectedContacts, setSelectedContacts] = useState(null);
    const [mailBody, setMailBody] = useState('');

    useEffect(() => {
        getAllContacts();
    }, []);

    const onUpload = (e) => {
        console.log("e: ", e);
        toast.current.show({severity: 'info', summary: 'Success', detail: 'File Uploaded'});
    }

    const uploadHandler = ({files}) => {
        const [file] = files;
        const fileReader = new FileReader();

       fileReader.onload = (e) => {
           const file = e.target.result;
           const allLines = file.split(/\r\n|\n/);
           // Reading line by line
           allLines.forEach( (line, index) => {
               const name = line.substr(0, line.indexOf('<') - 2);
               let email = line.split('<')[1];
               email = email.substr(0, email.indexOf('>'));
               const contact = {
                   name: name,
                   email: email,
                   inCampaign: false,
                   isEmailSent: false,
                   linkClicked: false,
                   durationUntilClick: 0};
               addOrUpdateEntry(contact);
           });
        }
        fileReader.readAsText(file);

    };

    const addOrUpdateEntry = (entry) => {
        const entryPresent = contacts.find(contact => contact.email === entry.email);
        if(!entryPresent){
            setContacts(contacts => [...contacts, entry]);
            sendCreateContactRequest(entry);
        } else {}
    }

    const getAllContacts = () => {

        axios.get(GET_ALL_CONTACTS_URL).then(res => {
            if (res.status === RESPONSE_OK) {
                setContacts(res.data);
            }
        });

        //TODO: first row might be selected when data came from back-end
        // this.onRowSelected(res.data.find(() => true))
    }

    const sendCreateContactRequest = (contact) => {
        axios.post(CREATE_CONTACT_URL, contact).then(res => {
            console.log(res);});
    }


    const sendUpdateContactRequest = (contact) => {
        axios.put(CREATE_CONTACT_URL, contact).then(res => {
            console.log(res);});
    }

/*    showFile = async (e) => {
        e.preventDefault()
        const reader = new FileReader()
        reader.onload = async (e) => {
            const text = (e.target.result)
            console.log(text)
            alert(text)
        };
        reader.readAsText(e.target.files[0])
    }*/
/*    const uploadInvoice = async (invoiceFile) => {
        let formData = new FormData();
        formData.append('invoiceFile', invoiceFile);

        const response = await fetch(`api/file/`,
            {
                method: 'POST',
                body: formData
            },
        );
    };*/

    const sendEmailRequest = (mailString) => {
        axios.post(SEND_EMAIL_URL, mailString).then(res => {
            console.log(res);
        })
    }

    const  onSendEmail = () => {
        //TODO: toastr
        if(selectedContacts && (mailBody !== '')){
            const contactEmails = [];
            selectedContacts.forEach(contact => {
                //FIXME: maybe better way to write this logic
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


    return (
        <div>
            <Toast ref={toast}></Toast>

            <DataTable className="p-datatable-sm" scrollable
                       value={contacts}
                       editMode="row"
                       dataKey="email"
                       selection={selectedContacts}
                       onSelectionChange={e => setSelectedContacts(e.value)}
                       selectionMode={"multiple"}
                       onSe
            >
                <Column selectionMode="multiple" headerStyle={{ width: '3em' }} ></Column>
                <Column field="name" header="Name"/>
                <Column field="email" header="E Mail"/>
                <Column field="isEmailSent" header="E Mail Sent"/>
                <Column field="campaign" header="In Campaign"/>
                <Column field="linkClicked" header="Link Has Clicked"/>
                <Column field="duration" header="Duration Link Click"/>

            </DataTable>

            <h5>E Mail </h5>
            <InputTextarea value={mailBody} onChange={(e) => setMailBody(e.target.value)} rows={10} cols={60} />
            <Button label="Submit" icon="pi pi-check"  onClick={ onSendEmail}/>

            <Tooltip target=".custom-choose-btn" content="Choose" position="bottom" />
            <Tooltip target=".custom-upload-btn" content="Upload" position="bottom" />
            <Tooltip target=".custom-cancel-btn" content="Clear" position="bottom" />

            <div className="card">
                <h5>Upload Contacts File</h5>
                <FileUpload name="demo[]" customUpload= {true}  onUpload={onUpload} uploadHandler={uploadHandler}
                            multiple={false} accept="image/*" maxFileSize={1000000}
                            emptyTemplate={<p className="m-0">Drag and drop files to here to upload.</p>}  />

            </div>
        </div>
    )
}
