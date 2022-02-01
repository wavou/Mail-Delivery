import React from 'react';
import 'primeflex/primeflex.css'
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import {Contacts} from "./components/Contacts";

const App = () =>  {

    return (
        <div>
          <h1>Mail App</h1>
          <Contacts/>
        </div>
    )
}

export default App;