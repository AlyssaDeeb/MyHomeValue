import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { setPropDetails, setCookieID } from '../actions/propDetailsActions';
import Header from '../components/Header';
import variables from '../config/config.json';
import Cookies from 'universal-cookie';

const cookies = new Cookies();

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
}

class SavedHomes extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            saved_homes: [],
            homesChecked: false
        };
    }

    // Get the list of saved home for this specific user ID
    getSavedHomes() {
        console.log(this.props.propDetails.cookieID);
        if (this.props.propDetails.cookieID == "0") {
            this.props.propDetails.cookieID = cookies.get("user");
        }
        var url = variables.baseURL + "/BackendServlet?type=savedHomes&userID=" + this.props.propDetails.cookieID;
        axios.get(url).then(response => {
            this.updateSaved(response.data)
            }
        );

    }

    // Assigns the passes data to the saved homes list
    updateSaved(savedList) {
        this.setState({
            saved_homes: savedList,
            homesChecked: true
        })
    }

    // Creates table to display list of saved homes
    addrows() {
        var savedHomes = [];

        if(this.state.saved_homes.length == 0 || this.state.saved_homes == "['empty']"){
            savedHomes.push(<tr key={0}><td>No Saved Homes</td></tr>);

        } else {
            savedHomes.push(<tr key={0}>
                <th></th>
                <th>Address</th>
                <th>SqFt </th>
                <th>Bed </th>
                <th>Bath</th>
                </tr>);
                for (var i = 0; i < this.state.saved_homes.length; i++) {
                    savedHomes.push(<tr key={i+1}>
                        <td><img style={{width: 75, height: 60}} src={this.state.saved_homes[i].image_url}/></td>
                        <td><a href="#address">{this.state.saved_homes[i].address}</a></td>
                        <td>{this.state.saved_homes[i].sq_ft}</td>
                        <td>{this.state.saved_homes[i].bed}</td>
                        <td>{this.state.saved_homes[i].bath}</td>
                        </tr>);
                    }
                }
                return savedHomes;
    }

render () {
    if (this.state.saved_homes.length == 0){
        this.getSavedHomes();
    }
    return (
        <div>
            <Header />
            <div className="container improvehomevalue-container pb-20" cellPadding="50px">
                <table id="homes" className="table">
                    <tbody>
                        {this.addrows()}
                    </tbody>
                </table>
            </div>
        </div>
        );
    }
};

export default connect(mapStateToProps)(SavedHomes);
