import React from 'react';
import Home from '../components/Home';
import AdminPage from '../components/AdminPage';
import PropertyDetails from '../components/PropertyDetails';
import CostCalculator from '../components/CostCalculator';
import ImproveHomeValue from '../components/ImproveHomeValue';
import Improvement from '../components/Improvement';
import WhatsNearby from '../components/WhatsNearby';
import NearbyHomes from '../components/NearbyHomes';
import SavedHomes from '../components/SavedHomes';
import Map from '../components/Map';
import Header from '../components/Header';
import Footer from '../components/Footer';
import NotFoundPage from '../components/NotFoundPage';
import { BrowserRouter, Route, Switch, Link, NavLink } from 'react-router-dom';
import { setPropDetails } from '../actions/propDetailsActions';
import SetPropStates from '../app.js'



const Improve1 = (props) => {
    return (
        <Improvement
        type="bedroom"
        {...props}
        />
    );
};

const Improve2 = (props) => {
    return (
        <Improvement
        type="bathroom"
        {...props}
        />
    );
};

const Improve3 = (props) => {
    return (
        <Improvement
        type="kitchen"
        {...props}
        />
    );
};

const AppRouter = () => (
    <BrowserRouter>
        <div>
            <SetPropStates/>

            <Switch>
                <Route path="/" component={Home} exact={true} />
                <Route path="/admin" component={AdminPage} />
                <Route path="/property-details" component={PropertyDetails} />
                <Route path="/nearby-homes" component={NearbyHomes} />
                <Route path="/cost-calculator" component={CostCalculator} />
                <Route path="/improve-home-value" component={ImproveHomeValue} />
                <Route path="/whats-nearby"  component={WhatsNearby}  />
                <Route path="/saved-homes" component={SavedHomes} />
                <Route path="/improvement1" component={Improve1} />
                <Route path="/improvement2" component={Improve2} />
                <Route path="/improvement3" component={Improve3} />
                <Route component={NotFoundPage} />
            </Switch>
            <Footer />
        </div>
    </BrowserRouter>
);

export default AppRouter;
