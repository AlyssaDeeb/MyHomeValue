import React, {PropTypes} from 'react';
import {Route, Link} from 'react-router-dom';

export default function NavItemSavedHomes({children, to, exact}) {
    return (
        <Route path={to} exact={exact} children={({match}) => (
         
                <Link to={to}>{children}</Link>

        )}/>
    )
}
