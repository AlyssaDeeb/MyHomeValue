import React from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom';

import { connect } from 'react-redux';
import { setPropDetails } from '../actions/propDetailsActions';
import HomeHeader from '../components/HomeHeader';
import variables from '../config/config.json';
import { withRouter } from "react-router-dom";

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
  }

class Home extends React.Component {
    constructor(props) {
        super(props);
    }

    // add commas to format currency
    addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
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
                <HomeHeader />
                <div className="row">
                    <div className="responsiveImage" id="responsiveImage">
                    <form className="form search welcomeSearch" id="responsiveForm" action="." onSubmit={this.searchAddress.bind(this)}>
                        <div className="col-sm-8 col-xs-10 col-sm-offset-2 easycenter welcometitle">
                            My Home Value<sup>&reg;</sup>
                        </div>
                        <div className="form-group col-sm-8 col-xs-10 col-sm-offset-2">
                            <div className="input-group">
                                <input className="form-control" id="navSearch" placeholder="Search for an address..."/>
                                    <i className="fa fa-search left-icon"></i>
                                <input type="submit" className="nodisplay" />
                            </div>
                        </div>
                    </form>
                </div>
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps)(Home);
