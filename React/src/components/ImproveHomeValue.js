import React from 'react';
import RecommendedBlock from './RecommendedBlock';
import { NavLink } from 'react-router-dom';
import axios from 'axios';
import { connect } from 'react-redux';
import { setPropDetails } from '../actions/propDetailsActions';
import Header from '../components/Header';

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
  }

class ImproveHomeValue extends React.Component {
    state = {
        improvedValue: "--"
    };

    // add commas to format currency
    addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    }

    render () {
        return (
            <div>
                <Header />
            <div className="container improvehomevalue-container">
                <div className="row">
                    <div className="improvedValue">
                        ${this.addCommas(this.props.propDetails.value)}
                        <span className="recommendedText"><br />Recommended:</span>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-4 col-sm-4 col-xs-12 recommended-block-container">
                        <NavLink to="/improvement1"><RecommendedBlock roi={10000} improvedValue={this.props.propDetails.value} name="Add a Bedroom" img={require('./../img/Bedroom.png')} /></NavLink>
                    </div>
                        <div className="col-md-4 col-sm-4 col-xs-12 recommended-block-container">
                    <NavLink to="/improvement2"><RecommendedBlock roi={30000} improvedValue={this.props.propDetails.value} name="Add a Bathroom" img={require('./../img/Bathroom.png')} /></NavLink>
                    </div>
                    <div className="col-md-4 col-sm-4 col-xs-12 recommended-block-container">
                        <NavLink to="/improvement3"> <RecommendedBlock roi={9000} improvedValue={this.props.propDetails.value} name="Remodel Kitchen" img={require('./../img/Kitchen.png')} /></NavLink>
                    </div>
                </div>
            </div>
            </div>
        );
    }
};

export default connect(mapStateToProps)(ImproveHomeValue);
