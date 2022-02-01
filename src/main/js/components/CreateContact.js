import {classNames} from "primereact/utils";
import React, {useEffect, useRef, useState} from "react";
import {Button} from "primereact/button";
import {InputText} from "primereact/inputtext";
import {Dialog} from "primereact/dialog";
import {Toast} from "primereact/toast";

const CreateContact = (props) => {

    const toast = useRef(null);

    let emptyContact = {
        name: '',
        email: '',
        inCampaign: false,
        isEmailSent: false,
        clickedLink: false,
        durationUntilClick: 0
    }

    useEffect(() => {
        setContact(props.selectedContact);
    }, [props.selectedContact])


    const [submitted, setSubmitted] = useState(false);
    const [contact, setContact] = useState(emptyContact);


    const hideDialog = () => {
        setSubmitted(false);
        props.hideDialog();
    }

    const saveContact = () => {
        setSubmitted(true);

        if (contact.name.trim()) {
            let _contact = {...contact};
            if (contact.id) {
                props.update(_contact);
                toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Contact Updated', life: 3000 });
            } else {
                props.create(_contact);
                toast.current.show({ severity: 'success', summary: 'Successful', detail: 'Contact Created', life: 3000 });
            }

            props.hideDialog();
            setContact(emptyContact);
        }
    }

    const onInputChange = (e, name) => {
        const val = (e.target && e.target.value) || '';
        let _contact = {...contact};
        _contact[`${name}`] = val;

        setContact(_contact);
    }



    const contactDialogFooter = (
        <React.Fragment>
            <Button label="Cancel" icon="pi pi-times" className="p-button-text" onClick={hideDialog} />
            <Button label="Save" icon="pi pi-check" className="p-button-text" onClick={saveContact} />
        </React.Fragment>
    );

    return(
        <div>
            <Toast ref={toast} />
            <Dialog visible={props.contactDialog} style={{ width: '450px' }} header="Contact Details" modal
                                         className="p-fluid" footer={contactDialogFooter} onHide={hideDialog}>
                <div className="field">
                    <label htmlFor="name">Name</label>
                    <InputText id="name" value={contact.name} onChange={(e) => onInputChange(e, 'name')}
                               required autoFocus className={classNames({ 'p-invalid': submitted && !contact.name })} />
                    {submitted && !contact.name && <small className="p-error">Name is required.</small>}
                </div>
                <div className="formgrid grid">
                    <div className="field col">
                        <label htmlFor="email">E mail</label>
                        <InputText id="email" value={contact.email} onChange={(e) => onInputChange(e, 'email')}
                                   required autoFocus className={classNames({ 'p-invalid': submitted && !contact.email })} />
                        {submitted && !contact.email && <small className="p-error">E mail is required.</small>}
                    </div>
                </div>
            </Dialog>
        </div>
    );
}

export  default CreateContact;