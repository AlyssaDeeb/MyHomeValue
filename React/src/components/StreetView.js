import ReactStreetview from 'react-streetview';
import React from 'react';
import { connect } from 'react-redux';

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
}

class StreetView extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            position: null,
            pov: null,
        };
    }
   
    render() {
    const googleMapsApiKey = 'INSERT STREET VIEW API KEY';

    const streetViewPanoramaOptions = {
        position: { lat: this.props.propDetails.lat, lng: this.props.propDetails.long },
        pov: { heading: 100, pitch: 0 },
        zoom: 1
    };

    return (
        <ReactStreetview
        apiKey={googleMapsApiKey}
        streetViewPanoramaOptions={streetViewPanoramaOptions}
        onPositionChanged={position => this.setState({ position: position })}
        onPovChanged={pov => this.setState({ pov: pov })}
        />
    )
    }
}

export default connect(mapStateToProps)(StreetView);