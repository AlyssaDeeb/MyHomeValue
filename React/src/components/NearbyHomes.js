import React from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom';
import ReactStreetview from 'react-streetview';
import { connect } from 'react-redux';
import { setPropDetails, setCookieID, setSavedHome } from '../actions/propDetailsActions';
import Header from '../components/Header';
import variables from '../config/config.json';
import MapComps from '../components/MapComps';
import StreetView from '../components/StreetView';

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
}

class NearbyHomes extends React.Component {
    constructor(props) {
        super(props);
        console.log(this.props.propDetails.comparables);
    }

    // add commas to format currency
    addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    }

    // generate html to populate comparables table
    addComparablesRows() {
        var comparableHomes = [];
        for (var i = 0; i < this.props.propDetails.comparables.length && i < 6; i++) {
            comparableHomes.push(<tr key={i}>
                <td><a href="#address">{this.props.propDetails.comparables[i].shortAddress.replace(/\w\S*/g, function (txt) { return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase(); })}</a></td>
                <td>${this.addCommas(this.props.propDetails.comparables[i].price)}</td>
                <td>{this.props.propDetails.comparables[i].squareFeet}</td>
                <td>{this.props.propDetails.comparables[i].bedrooms}</td>
                <td>{this.props.propDetails.comparables[i].bathrooms}</td>
            </tr>);
        }
        return comparableHomes;
    }

    render() {
        return (
            <div>
                <Header />
                <div className="container-fluid vertical-parent">
                    <MapComps />
                    <div className="row">
                        <div className="col-xs-12 col-md-8 col-md-offset-2">
                            <div className="panel-group" id="accordion3">
                                <div className="panel panel-default" data-toggle="collapse" data-parent="#accordion" data-target="#collapseOne">
                                    <div className="panel-heading" id="headingOneHundred">
                                        <div className="panel-title">
                                            <a className="collapsed" role="button" href="javascript:">
                                                Nearby Homes
                                        </a>
                                        </div>
                                    </div>
                                    <div id="collapseOne" className="panel-collapse in">
                                        <div className="panel-body">
                                            <div className="row">
                                                <table id="homes" className="table">
                                                    <tbody>
                                                        <tr>
                                                            <th scope="col">Address</th>
                                                            <th scope="col">Sale Price </th>
                                                            <th scope="col">SqFt </th>
                                                            <th scope="col">Bed </th>
                                                            <th scope="col">Bath</th>
                                                        </tr>
                                                        {this.addComparablesRows()}
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps)(NearbyHomes);
