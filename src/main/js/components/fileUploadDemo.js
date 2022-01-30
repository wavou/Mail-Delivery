import React, {useEffect, useRef, useState} from 'react';
import { Toast } from 'primereact/toast';
import { FileUpload } from 'primereact/fileupload';

import { Tooltip } from 'primereact/tooltip';
import axios from "axios";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {CREATE_CONTACT_URL, GET_ALL_CONTACTS_URL, RESPONSE_OK} from "../constants/constants";


export const FileUploadDemo = () => {

    const toast = useRef(null);

    const [contacts, setContacts] = useState([]);
    const [selectedItem, setSelectedItem] = useState(null);

    useEffect(() => {
        getAllContacts();
    }, []);

    const onUpload = (e) => {
        console.log("e: ", e);
        toast.current.show({severity: 'info', summary: 'Success', detail: 'File Uploaded'});
    }

    const invoiceUploadHandler = ({files}) => {
        const fileReader = new FileReader();

       fileReader.onload = (e) => {
           const file = e.target.result;
           const allLines = file.split(/\r\n|\n/);
           // Reading line by line
           allLines.forEach( (line, index) => {
               const name = line.substr(0, line.indexOf('<') - 2);
               let email = line.split('<')[1];
               email = email.substr(0, email.indexOf('>'));
               const entry = {name: name, email: email};
               addOrUpdateEntry(entry);
           });
        }
        fileReader.readAsText(files);

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

    return (
        <div>
            <Toast ref={toast}></Toast>

            <DataTable className="p-datatable-sm" scrollable
                       value={contacts}
                       selection={selectedItem}
                       editMode="row"
                       dataKey="email"
            >
                <Column field="name" header="Name"/>
                <Column field="email" header="E Mail"/>

            </DataTable>

            <Tooltip target=".custom-choose-btn" content="Choose" position="bottom" />
            <Tooltip target=".custom-upload-btn" content="Upload" position="bottom" />
            <Tooltip target=".custom-cancel-btn" content="Clear" position="bottom" />

            <div className="card">
                <h5>Advanced</h5>
                <FileUpload name="demo[]" customUpload= {true}  onUpload={onUpload} uploadHandler={invoiceUploadHandler} multiple accept="image/*" maxFileSize={1000000}
                            emptyTemplate={<p className="m-0">Drag and drop files to here to upload.</p>}  />

            </div>
        </div>
    )
}
