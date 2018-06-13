import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';

const mapStateToProps = (state) => {
    console.log(state);
    return {
        coordinates: state.coordinates,
        propDetails: state.propDetails
    };
  }

class Places extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            name: [],
            category: 'Category: Restaurants'
        };
        this.handleClick = this.handleClick.bind(this);

        const client_id = "INSERT FOURSQUARE CLIENT ID";
        const client_secret = "INSERT FOURSQUARE CLIENT ID";
        const v = "INSERT DATE API OBTAINED";

        axios.get('https://api.foursquare.com/v2/venues/search?ll=' + Number(this.props.propDetails.lat) + ','+ Number(this.props.propDetails.long) + '&client_id=' + client_id + '&client_secret=' + client_secret + '&v=' + v + '&limit=10&query=' + 'restaurant') //this.state.query)
            .then(response => this.setState({ name: response.data.response.venues }));
    }

    handleClick = (param) => (e) => {
        if(param === 'restaurants' ) {
            this.setState({category: 'Category: Restaurants'})
        }
        else if(param === 'bar club' ) {
            this.setState({category: 'Category: Night Life'})
        }
        else if(param === 'shopping' ) {
            this.setState({category: 'Category: Shopping'})
        }
        else if(param === 'sports recreational' ) {
            this.setState({category: 'Category: Activities'})
        }
        else if(param === 'parks' ) {
            this.setState({category: 'Category: Parks'})
        }
        else if(param === 'hospitals' ) {
            this.setState({category: 'Category: Hospitals'})
        }

        const client_id = "INSERT FOURSQUARE CLIENT ID";
        const client_secret = "INSERT FOURSQUARE CLIENT ID";
        const v = "INSERT DATE API OBTAINED";

        axios.get('https://api.foursquare.com/v2/venues/search?ll=' + this.props.propDetails.lat +','+ this.props.propDetails.long +'&client_id=' + client_id + '&client_secret=' + client_secret + '&v=' + v + '&limit=10&query=' + param) //this.state.query)
            .then(response => this.setState({ name: response.data.response.venues }));
    }

    render() {
        return (
            <div className="places">
                <div className="dropdown category">
                    <div id="dLabel" className="insetButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <h2 style={{color: 'black', fontSize: '150%', textAlign: 'left', textIndent: '0%'}}>{this.state.category} <i className="fa fa-caret-down"></i></h2>
                    </div>
                    <ul className="dropdown-menu" aria-labelledby="dLabel">
                        <li>
                            <a onClick={this.handleClick('restaurants')}>Restaurants</a>
                        </li>
                        <li>
                            <a onClick={this.handleClick('bar club')}>Night Life</a>
                        </li>
                        <li>
                            <a onClick={this.handleClick('shopping')}>Shopping</a>
                        </li>
                        <li>
                            <a onClick={this.handleClick('sports recreational')}>Activities</a>
                        </li>
                        <li>
                            <a onClick={this.handleClick('parks')}>Parks</a>
                        </li>
                        <li>
                            <a onClick={this.handleClick('hospitals')}>Hospitals</a>
                        </li>
                    </ul>
                </div>
                <br></br>
                {this.state.name.map(e => (
                    <div>
                        <p><b> {e.name}</b> <br></br> {e.location.formattedAddress[0]} {e.location.formattedAddress[1]}  </p>
                        <br></br>
                    </div>
                ))}
            </div>
        );
    };
}

export default connect(mapStateToProps)(Places);
