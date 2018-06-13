import React from 'react';
import { NavLink } from 'react-router-dom';
import NavItem from './NavItem';
import NavItemSavedHomes from './NavItem';
import axios from 'axios';
import variables from '../config/config.json';
import { withRouter } from "react-router-dom";


export default class Header extends React.Component {

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
        var addressLine = e.target[0].value.substring(0, e.target[0].value.indexOf(','));
        var zip = e.target[0].value.substring(e.target[0].value.length-5, e.target[0].value.length);
        var url = variables.baseURL + "/BackendServlet?type=address&addressLine=" + encodeURIComponent(addressLine) + "&zipCode=" + zip + "&userID=" + this.props.propDetails.cookieID;

        axios.get(url).then(response => this.props.dispatch(setPropDetails({id: this.getNewIDFromData(response.data)})));
        this.props.history.push("/property-details");
        e.preventDefault();
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
                                <form name="mobileForm">
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

                                </div>
                            </div>
                            <div className="collapse navbar-collapse" id="example-navbar-collapse-3">
                                <ul className="nav navbar-nav navbar-right">
                                    <NavItemSavedHomes to="/saved-homes" children="Saved Homes" />
                                    <NavItemSavedHomes to="/admin" children="Admin Page" />
                                </ul>
                                <i className="fa fa-times"></i>
                            </div>
                        </div>
                    </nav>
                </div>
            </div>
        );
    }
}
