import React from 'react';
import Map from './Map';
import Places from './Places';
import axios from 'axios';
import { connect } from 'react-redux';
import Header from '../components/Header';
import variables from '../config/config.json';

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        coordinates: state.coordinates,
        propDetails: state.propDetails

    };
  }

class WhatsNearby extends React.Component {

    render() {

        return (
            <div>
                <Header />
            <div className="containter-fluid whatsnearby_">
                <div className="row">
                    <div className="col-md-6">
                        <Map />
                    </div>
                    <div className="col-md-6">
                        <Places /*lat={this.state.latitude} lng={this.state.longitude} *//>
                    </div>
                </div>
            </div>
            </div>
        );
    }
}

export default connect(mapStateToProps)(WhatsNearby);
