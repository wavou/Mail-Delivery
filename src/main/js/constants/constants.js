const CONTROLLER_PREFIX = 'api/';

export const CREATE_CONTACTS_URL = CONTROLLER_PREFIX + 'create-contacts';
export const GET_ALL_CONTACTS_URL = CONTROLLER_PREFIX + 'contacts';
export const CREATE_CONTACT_URL = CONTROLLER_PREFIX + 'create-contact';
export const UPDATE_CONTACT_URL = CONTROLLER_PREFIX + 'update-contact';
export const DELETE_CONTACTS_URL = CONTROLLER_PREFIX + 'delete-contacts';


export const SEND_EMAIL_URL = CONTROLLER_PREFIX + 'send-email';

export const RESPONSE_OK = 200;

export const  EMPTY_CONTACT = {
    name: '',
    email: '',
    inCampaign: false,
    isEmailSent: false,
    clickedLink: false,
    durationUntilClick: 0
};
