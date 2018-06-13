import React from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom';
import ReactStreetview from 'react-streetview';
import { connect } from 'react-redux';
import { setPropDetails, setCookieID, setSavedHome, setLoading } from '../actions/propDetailsActions';
import Header from '../components/Header';
import variables from '../config/config.json';
import MapComps from '../components/MapComps';
import StreetView from '../components/StreetView';
import LoadingScreen from 'react-loading-screen';
import { React_Bootstrap_Carousel } from "react-bootstrap-carousel";
import "react-bootstrap-carousel/dist/react-bootstrap-carousel.css";

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
}

class PropertyDetails extends React.Component {
    constructor(props) {
        super(props);
        console.log(this.props.propDetails.comparables);

        this.state = {
            ppsf: "--",
            position: null,
            pov: null,
            saved: this.props.propDetails.homeSaved,
            loading: true
        };
    }

    // adds commas for currency formatting
    addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    }

    // toggles save/remove current property for the current user in the backend database 
    saveHome() {
        var url = variables.baseURL + "/BackendServlet?type=" + (this.props.propDetails.homeSaved ? "remove" : "save") + "Home&id=" + this.props.propDetails.id + "&userID=" + this.props.propDetails.cookieID /*cookies.get("user")*/;
        console.log(url);
        axios.get(url).then(response => {
            console.log('Processed Saved Home')
        }
        );
        this.props.dispatch(setSavedHome({ homeSaved: !this.props.propDetails.homeSaved }));
        this.setState({ saved: !this.state.saved });
    }

    // populates html to generate comparable homes table
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

    // get the list of saved homes for the current user
    getSavedHomes() {
        console.log(this.props.propDetails.cookieID);
        var url = variables.baseURL + "/BackendServlet?type=savedHomes&userID=" + this.props.propDetails.cookieID;
        axios.get(url).then(response => {
            this.updateSaved(response.data)
        }
        );

    }

    // updates the saved home state of the current home
    updateSaved(savedList) {
        this.setState({
            saved_homes: savedList,
            homesChecked: true
        })
    }

    // if the property has not yet loaded, set the loading screen to display for 8 seconds
    componentDidMount(props) {
       if (this.props.propDetails.price != '') {

        setTimeout(() =>
        this.props.dispatch(setLoading({ loading: false }))
                , 8000);

        }
    }

    render() {
        return (
            <LoadingScreen
            loading={this.props.propDetails.loading}
            bgColor='#f1f1f1'
            spinnerColor='#F04730'
            textColor='#676767'
            logoSrc={require('./../img/logo.png')}
            text='Searching 147 million properties...'
            >

            <div>
                <Header />
                <div className="container-fluid vertical-parent">
                    <div className="row pt-30">
                        <div className="col-xs-12 col-md-8 col-md-offset-2">
                            <React_Bootstrap_Carousel autoplay={false} interval={null} >
                                <div>
                                    <img src={this.props.propDetails.url} className="homeImage" />
                                </div>
                                <div>
                                    <div className="streetview">
                                        <StreetView />
                                    </div>
                                </div>
                           </React_Bootstrap_Carousel>
                        </div>
                    </div>
                    <div className="row pt-30">
                        <div className="col-xs-12 col-md-8 col-md-offset-2">
                            <div className="panel panel-default">
                                <div className="panel-heading panel-title" id="headingOneHundred">
                                    <div className="row">
                                        <div className="panel-title col-md-7 col-sm-7 col-xs-12" style={{ textAlign: "start" }} >
                                            {this.props.propDetails.addressLine}
                                            <div>{this.props.propDetails.city}, {this.props.propDetails.state}, {this.props.propDetails.zip}</div>
                                        </div>
                                        <div className="col-md-4 col-sm-4 col-xs-12" >
                                            <span className={this.props.propDetails.growthPercent >= 0 ? "mainprice-up" : "mainprice-down"}>${this.addCommas(this.props.propDetails.price)}</span>
                                            <br />
                                            <div className="field">
                                                <img src={this.props.propDetails.growthPercent >= 0 ? require('./../img/growth.png') : require('./../img/decline.png')} className="icon" />
                                                <label>{this.props.propDetails.growthPercent}%</label>
                                                <div className="lastSoldText">Since last sold in {this.props.propDetails.saleDate}</div>
                                            </div>
                                        </div>
                                        <div className="col-md-1 col-sm-1 col-xs-12 heartbox">
                                            <img className="heart" src={this.state.saved ? require('./../img/heart.png') : require('./../img/heart-empty.png')} onClick={this.saveHome.bind(this)} />
                                            <div className="savetext" onClick={this.saveHome.bind(this)} >{this.state.saved ? "Saved" : "Save"}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-xs-12 col-md-8 col-md-offset-2">
                            <div className="panel-group" id="accordion2">
                                <div className="panel panel-default" data-toggle="collapse" data-parent="#accordion" data-target="#collapseOne">
                                    <div className="panel-heading" id="headingOneHundred">
                                        <div className="panel-title">
                                            <a className="collapsed" role="button" href="javascript:">
                                                Property Information
                                        </a>
                                        </div>
                                    </div>
                                    <div id="collapseOne" className="panel-collapse collapse in">
                                        <div className="panel-body">
                                            <div className="row">
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/house_type.png')} className="icon" />
                                                        <label>Home Type</label>
                                                        <div className="value">{this.props.propDetails.hometype}</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/sale.png')} className="icon" />
                                                        <label>Year Built</label>
                                                        <div className="value">{this.props.propDetails.yearbuilt}</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/bed.png')} className="icon" />
                                                        <label>Bedrooms</label>
                                                        <div className="value">{this.props.propDetails.bedrooms}</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/bath.png')} className="icon" />
                                                        <label>Bathrooms</label>
                                                        <div className="value">{this.props.propDetails.bathrooms}</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/sqft.png')} className="icon" />
                                                        <label>Square Footage</label>
                                                        <div className="value">{this.addCommas(this.props.propDetails.squarefootage)} Square Feet</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/acres.png')} className="icon" />
                                                        <label>Acreage</label>
                                                        <div className="value">{this.props.propDetails.acreage} Acres</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/stories.png')} className="icon" />
                                                        <label>Stories</label>
                                                        <div className="value">{this.props.propDetails.stories}</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/sqft_price.png')} className="icon" />
                                                        <label>Price/Square Foot</label>
                                                        <div className="value">${this.props.propDetails.ppsf} / Square Foot</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4 col-sm-4 col-xs-12">
                                                    <div className="field">
                                                        <img src={require('./../img/parking.png')} className="icon" />
                                                        <label>Parking Spaces</label>
                                                        <div className="value">{this.props.propDetails.parking}</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-xs-12 col-md-8 col-md-offset-2">
                            <div className="panel-group" id="accordion3">
                                <div className="panel panel-default" data-toggle="collapse" data-parent="#accordion" data-target="#collapseTwo">
                                    <div className="panel-heading" id="headingOneHundred">
                                        <div className="panel-title">
                                            <a className="collapsed" role="button" href="javascript:">
                                                Neighborhood
                                        </a>
                                        </div>
                                    </div>
                                    <div id="collapseTwo" className="panel-collapse collapse">
                                        <div className="panel-body">
                                            <div className="row">
                                                <div className="col-md-12">
                                                    <div className="easycenter dataprovidedby">
                                                        Data provided by <br/>
                                                        <img style={{ width: 150, height: 37 }} src={require('./../img/greatschools-logo.png')} className="material-icon" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-8">
                                                    <div className="field">
                                                        <label>Elementary School</label>
                                                        <div>{this.props.propDetails.ele_school} </div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4">
                                                    <div>
                                                        <div className="schoolrank">{this.props.propDetails.ele_school_rank} out of 10</div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-8">
                                                    <div className="field">
                                                        <label>Middle School</label>
                                                        <div>{this.props.propDetails.mid_school}</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4">
                                                    <div>
                                                        <div className="schoolrank"> {this.props.propDetails.mid_school_rank} out of 10 </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-8">
                                                    <div className="field">
                                                        <label>High School</label>
                                                        <div>{this.props.propDetails.high_school} </div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4">
                                                    <div>
                                                        <div className="schoolrank">{this.props.propDetails.high_school_rank} out of 10</div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-12">
                                                    <div className="easycenter dataprovidedby">
                                                        Data provided by <br/>
                                                        <img style={{ width: 150 }} src={'https://cdn2.walk.sc/2/images/press/walk-score-logo-large.png'} className="material-icon" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col-md-8">
                                                    <div className="field">
                                                        <div>{this.props.propDetails.walkScore_desc}</div>
                                                    </div>
                                                </div>
                                                <div className="col-md-4">
                                                    <div className="schoolrank">
                                                        {this.props.propDetails.walkScore} out of 100
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


            </div>

 </LoadingScreen>
        );
    }
}

export default connect(mapStateToProps)(PropertyDetails);
