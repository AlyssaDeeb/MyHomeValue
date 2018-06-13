import React from 'react';
import ReactDOM from 'react-dom';
import AppRouter from './routers/AppRouter';
import 'normalize.css/normalize.css';
import './styles/styles.scss';
import { Provider } from 'react-redux';
import configureStore from './store/configureStore';
import Cookies from 'universal-cookie';
import axios from 'axios';
import { connect } from 'react-redux';
import { setPropDetails, setLoading } from './actions/propDetailsActions';
import {setCookieID} from './actions/propDetailsActions';
import variables from './config/config.json';

const cookies = new Cookies();

const store = configureStore();

const mapStateToProps = (state) => {
    return {
        propDetails: state.propDetails
    };
  }

//Pass in property Id from server here, this will update anywhere id is used. (PropertyDetails only currently).
try {
    store.dispatch(setPropDetails({id: this.props.propDetails.id}));
}
catch (error) {
    store.dispatch(setPropDetails({id: "INSERT DUMMY CORELOGIC ID"}));
}

store.dispatch(setLoading({ loading: true }));


class SetPropStates extends React.Component {
    constructor(props) {
        super(props);
    }

    getPropertyObject() {
        if (this.props.propDetails.bedrooms == '---') {
            var url = variables.baseURL + "/BackendServlet?id=" + this.props.propDetails.id + "&type=propertyInfo&userID=" + cookies.get("user");
            axios.get(url).then(response => this.updatePage(response.data));
        }
    }

    updatePage(propertydata) {
        this.props.dispatch(setCookieID({cookieID: cookies.get("user")})) // set Cookie

        //Setting States to the store
        this.props.dispatch(setPropDetails(
            {
                id: propertydata.id,
                hometype: propertydata.type,
                yearbuilt: propertydata.yearBuilt,
                bedrooms: propertydata.bedrooms,
                bathrooms: propertydata.bathrooms,
                squarefootage: propertydata.squareFeet,
                ppsf: (propertydata.value/propertydata.squareFeet).toFixed(2),
                acreage: propertydata.acres,
                parking: propertydata.parkingSpaces,
                stories: propertydata.stories,
                value: propertydata.value,
                addressLine: propertydata.addressLine,
                city: propertydata.city,
                state: propertydata.state,
                zip: propertydata.zip,
                price: propertydata.value,
                saleDate: propertydata.soldYear,
                growthPercent: propertydata.growthRate,
                ele_school: propertydata.schools.Elementary.Name,
                mid_school: propertydata.schools.Middle.Name,
                high_school: propertydata.schools.High.Name,
                ele_school_rank: propertydata.schools.Elementary.Rating,
                mid_school_rank: propertydata.schools.Middle.Rating,
                high_school_rank: propertydata.schools.High.Rating,
                walkScore: propertydata.walkscore.walkscore,
                transitScore: propertydata.walkscore.transitscore,
                walkScore_desc: propertydata.walkscore.walkscore_desc,
                url: propertydata.url,
                lat: propertydata.latitude,
                long: propertydata.longitude,
                comparables: propertydata.comparables,
                homeSaved: propertydata.homeSaved,
                loading: propertydata.loading
            }
        ))

    }

    // Checks if this browser already has a cookie created, if not create new user 
    checkCookies() {
        if (cookies.get("user") == undefined) {
            var url = variables.baseURL + "/BackendServlet?type=getNewUserID";
            axios.get(url).then(response => this.setCookies(response.data.user))
        }
    }

    // Assign new user ID to cookie
    setCookies(info) {
        cookies.set("user", info);
    }

    render() {
        //Will only call method when id is not empty. Id will be set to empty once it's called.
        //This will make it so it uses the current states
        //instead of placing API calls every time someone clicks on Propety Details.

        this.checkCookies();
        //cookies.remove("user");

        if(this.props.propDetails.id != "") {
            this.getPropertyObject();         
        }

        return null;
    };
};

const jsx = (
    <div>
    <Provider store={store}>
        <AppRouter />
    </Provider>
    </div>
);

export default connect(mapStateToProps)(SetPropStates);
ReactDOM.render(jsx, document.getElementById('app'));
