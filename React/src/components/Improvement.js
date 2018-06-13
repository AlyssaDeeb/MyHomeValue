import React from 'react';
import RecommendedBlock from './RecommendedBlock';
import axios from 'axios';
import { connect } from 'react-redux';
import { setPropDetails } from '../actions/propDetailsActions';
import Header from '../components/Header';
import variables from '../config/config.json';


const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
  }

class Improvement extends React.Component {

    constructor(props) {
        super(props);
        console.log(props.type);
        switch (this.props.type) {
            case 'bedroom':
                this.state = {
                    original_value: this.props.propDetails.value,
                    value_added: "250",
                    cost_factor: "150",
                    roi: 10000,
                    improvedValue: this.props.propDetails.value + 25000,
                    squareFeet: 100,
                    improvementName: "Add a Bedroom",
                    improvementImage: require("./../img/Bedroom.png")
                };
                break;
            case 'bathroom':
                this.state = {
                    original_value: this.props.propDetails.value,
                    value_added: "640",
                    cost_factor: "40",
                    roi: 30000,
                    improvedValue: this.props.propDetails.value + 32000,
                    squareFeet: 50,
                    improvementName: "Add a Bathroom",
                    improvementImage: require("./../img/Bathroom.png")
                };
                break;
            case 'kitchen':
                this.state = {
                    original_value: this.props.propDetails.value,
                    value_added: "90",
                    cost_factor: "60",
                    roi: 9000,
                    improvedValue: this.props.propDetails.value + 27000,
                    squareFeet: 300,
                    improvementName: "Add a Kitchen",
                    improvementImage: require("./../img/Kitchen.png")
                };
                break;
            default:
                this.state = {
                    original_value: this.props.propDetails.value,
                    value_added: "10",
                    cost_factor: "10",
                    roi: 0,
                    improvedValue: 1003000,
                    squareFeet: 300,
                    improvementName: "Add an Improvement",
                    improvementImage: require("./../img/Bedroom.png")
                };
        }
    }

    calculate = (e) => {

        var newROI = ( parseFloat(this.state.value_added)-parseFloat(this.state.cost_factor) )*this.state.squareFeet;

        var newValue = this.state.original_value + (parseFloat(this.state.value_added))*this.state.squareFeet;

        this.setState({
            roi: newROI,
            improvedValue: newValue
        });
        e.preventDefault();
    }


    handleChange = (e) => {
        if (e.target.value.substring(0, 1)=="$") {
            this.setState({
                [e.target.name]: e.target.value.substring(1, e.target.value.length)
            });
        }
        else {
            this.setState({
                [e.target.name]: e.target.value
            });
        }
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
                        <div className={this.state.roi >= 0 ? "improvedValue-green" : "improvedValue-red"}>
                            ${this.addCommas(this.state.improvedValue)}
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-4 col-sm-4 col-xs-12 recommended-block-container">
                            <span className="recommendedText"><br />Chosen:</span>
                            <RecommendedBlock name={this.state.improvementName} img={this.state.improvementImage} improvedValue={this.state.improvedValue} basic="true" />
                            <div><br />Add <input type="text" pattern="\d*" name="squareFeet" value={this.state.squareFeet} onChange={this.handleChange} onKeyUp={this.calculate} className="sqftinput"/> sqft.</div>
                        </div>
                        <div className="col-md-8 col-sm-8 col-xs-12 calcarea" align="center">
                            <form className="improvementCalcForm" onKeyUp={this.calculate}>
                                <table className="calctable">
                                    <tbody>
                                        <tr className="calcrow">
                                            <td width="10%">+</td>
                                            <td width="20%">${this.addCommas(this.state.value_added*this.state.squareFeet)}</td>
                                            <td width="20%">value</td>
                                            <td width="30%"><input type="text" pattern="\d*" className="userinput" name="value_added" value={"$"+this.state.value_added} onChange={this.handleChange} /></td>
                                            <td width="20%">/sqft.</td>
                                        </tr>
                                        <tr className="calcrow">
                                            <td width="10%">-</td>
                                            <td width="20%">${this.addCommas(this.state.cost_factor*this.state.squareFeet)}</td>
                                            <td width="20%">cost</td>
                                            <td width="30%"><input type="text" pattern="\d*" className="userinput" name="cost_factor" value={"$"+this.state.cost_factor} onChange={this.handleChange} /></td>
                                            <td width="20%">/sqft.</td>
                                        </tr>
                                        <tr>
                                            <td colSpan="5"><hr /></td>
                                        </tr>
                                        <tr>
                                            <td colSpan="5">
                                                <div className={this.state.roi >= 0 ? "roi-text-green" : "roi-text-red"}><img src={this.state.roi >=0 ? require('./../img/uparrow.png') : require('./../img/downarrow.png')} className="arrow" />${this.addCommas(this.state.roi.toFixed(2).substring(this.state.roi.toString().indexOf('-')+1, this.state.roi.toString().length))} ({ this.addCommas(parseFloat(100*this.state.roi/(this.state.improvedValue - this.state.roi)).toFixed(2)) }&#37;)</div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
};

export default connect(mapStateToProps)(Improvement);
