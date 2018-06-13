import React from 'react';
import { NavLink, browserHistory } from 'react-router-dom';
import NavItem from './NavItem';
import NavItemSavedHomes from './NavItem';
import axios from 'axios';
import { connect } from 'react-redux';
import { setPropDetails, setCookieID } from '../actions/propDetailsActions';
import variables from '../config/config.json';
import { withRouter } from "react-router-dom";

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
}

class Header extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            suggested: []
        };

        var url = variables.baseURL + "/BackendServlet?partial=110+End&type=typeAhead";
        if (this.state.suggested.length == 0) {
            axios.get(url).then(response => this.updateSuggested(response));
        }
        else {
            console.log(this.state);
        }
    }

    getPropertyObject() {
        var url = variables.baseURL + "/BackendServlet?id=" + this.props.propDetails.id + "&type=propertyInfo&userID=" + cookies.get("user");
        axios.get(url).then(response => this.updatePage(response.data));
    }

    updatePage(propertydata) {
        this.props.dispatch(setCookieID({cookieID: this.props.propDetails.cookieID})) // set Cookie

        //Setting States to the store
        this.props.dispatch(setPropDetails(
            {
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
                homeSaved: propertydata.homeSaved
            }
        ))

    }

    updateSuggested(data) {
        this.setState({suggested: data.data});
    }

    formatAddress(addressline) {
        return addressline.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}).substring(0, addressline.length-8) + addressline.substring(addressline.length-8, addressline.length);
    }

    getNewIDFromData(data) {
        console.log(data);
        return data.id;
    }

    searchAddress(e) {
        this.updateLoading;
        var addressLine = e.target[0].value.substring(0, e.target[0].value.indexOf(','));
        var zip = e.target[0].value.substring(e.target[0].value.length-5, e.target[0].value.length);
        var url = variables.baseURL + "/BackendServlet?type=address&addressLine=" + encodeURIComponent(addressLine) + "&zipCode=" + zip + "&userID=" + this.props.propDetails.cookieID;

        axios.get(url).then(response => this.props.dispatch(setPropDetails({id: this.getNewIDFromData(response.data)})));

        e.preventDefault();
    }

    updateLoading () {
        this.props.dispatch(setLoading({ loading: true }));
    }

    render() {
        {

            $(function () {
              window.clui.init();

              window.clui.autoCompleteInit('#mobileSearchbox', {
                minLength: 2,
                select: function () {
                  window.clui.mobileSearchClose();
                },
                source: function (request, response) {
                  const settings = {
                    async: true,
                    url: variables.baseURL + "/BackendServlet?type=typeAhead",
                    crossDomain: true,
                    data: {
                      partial: request.term
                    },
                    method: "GET"
                  };

                  $.ajax(settings).done(function (data) {
                    response($.map($.parseJSON(data), function (item) {
                        return {
                          label: item.address.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}).substring(0, item.address.length-8) + item.address.substring(item.address.length-8, item.address.length),
                          value: item.address.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}).substring(0, item.address.length-8) + item.address.substring(item.address.length-8, item.address.length)
                        };
                    }));
                  });
                }
              });

              window.clui.autoCompleteInit('#navSearch', {
                minLength: 2,
                select: function () {
                  window.clui.mobileSearchClose();
                },
                source: function (request, response) {
                  const settings = {
                    async: true,
                    url: variables.baseURL + "/BackendServlet?type=typeAhead",
                    crossDomain: true,
                    data: {
                      partial: request.term
                    },
                    method: "GET"
                  };

                  $.ajax(settings).done(function (data) {
                    response($.map($.parseJSON(data), function (item) {
                      return {
                        label: item.address.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}).substring(0, item.address.length-8) + item.address.substring(item.address.length-8, item.address.length),
                        value: item.address.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}).substring(0, item.address.length-8) + item.address.substring(item.address.length-8, item.address.length)
                      };
                    }));
                  });
                }
              });

            });
        }

        return (
            <div>
                <div>
                    <nav className="navbar navbar-default nav" role="navigation">
                        <div className="mobile-overlay"></div>
                        <div className="mobile-search">
                            <div className="mobile-searchbar">
                                <i className="fa fa-search"></i>
                                <form name="mobileForm" action="." onSubmit={this.searchAddress.bind(this)}>
                                    <label className="sr-only" htmlFor="mobileSearchbox">Address Search...</label>
                                    <input type="text" className="navbar-search" placeholder="Search something..." id="mobileSearchbox" />
                                    <input type="submit" className="nodisplay" />
                                </form>
                                <i className="fa fa-times"></i>
                            </div>
                        </div>
                        <div className="container-fluid">
                            <div className="navbar-collapse-btn">
                                <button type="button" className="navbar-toggle collapsed" data-toggle="collapse"
                                        data-target="#example-navbar-collapse-3">
                                    <span className="sr-only">Toggle navigation</span>
                                    <i className="fa fa-bars" aria-hidden="true"></i>
                                </button>
                            </div>
                            <button type="button" className="navbar-mobile-search">
                                <i className="fa fa-search" aria-hidden="true"></i>
                            </button>

                            <div className="navbar-header">
                                <a className="navbar-logo"href="/"></a>
                                <div className="application-name">
                                    <h1>My Home Value</h1>
                                </div>
                            </div>
                            <div className="collapse navbar-collapse" id="example-navbar-collapse-3">
                                <ul className="nav navbar-nav navbar-right">
                                <NavItemSavedHomes to="/saved-homes" children="Saved Homes" />
                                </ul>
                                <form className="navbar-form navbar-search" action="." onSubmit={this.searchAddress.bind(this)}>
                                    <div className="form-group">
                                        <div className="input-group ">
                                            <input className="form-control navbarsearch" id="navSearch" placeholder="Search for an address..."/>
                                            <i className="fa fa-search left-icon"></i>
                                            <input type="submit" className="nodisplay" />
                                        </div>
                                    </div>
                                </form>
                                <i className="fa fa-times"></i>
                            </div>
                        </div>
                    </nav>
                </div>
                <div className="secondary-nav">
                    <div className="container-fluid more">
                        <ul className="secondary-tabs">
                            <NavItem to="/property-details" children="Property Details" exact={true} />
                            <NavItem to="/nearby-homes" children="Nearby Homes" />
                            <NavItem to="/cost-calculator" children="Cost Calculator" />
                            <NavItem to="/improve-home-value" children="Improve Home Value" />
                            <NavItem to="/whats-nearby" children="What&apos;s Nearby" />
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps)(Header);
