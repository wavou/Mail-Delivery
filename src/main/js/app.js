import React from 'react';
import 'primeflex/primeflex.css'
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import {FileUploadDemo} from "./components/fileUploadDemo";

const App = () =>  {

    return (
        <div>
          <h1>Mail App</h1>
          <FileUploadDemo/>
        </div>
    )
}

export default App;