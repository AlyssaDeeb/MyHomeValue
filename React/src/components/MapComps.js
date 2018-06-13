import React from 'react';
import GoogleMapReact from 'google-map-react';
import { connect } from 'react-redux';

const mapStateToProps = (state) => {

  //console.log(state);
  return {
    propDetails: state.propDetails

  };
};

const Marker = ( { textAddress, textPrice } ) => {
  return <div><a href="#" data-toggle="tooltip" title={textAddress}> <img src={require('./../img/home_icon_2.png')} className="icon" />${textPrice}</a></div>
}
const Marker2 = ( { textAddress, textPrice } ) => {
  return <div><a href="#" data-toggle="tooltip" title={textAddress}> <img src={require('./../img/home_icon_2.png')} className="icon" />${textPrice}</a></div>
}
const Marker3 = ( {  textAddress, textPrice } ) => {
  return <div><a href="#" data-toggle="tooltip" title={textAddress}> <img src={require('./../img/home_icon_2.png')} className="icon" />${textPrice}</a></div>
}
const Marker4 = ( {  textAddress, textPrice } ) => {
  return <div><a href="#" data-toggle="tooltip" title={textAddress}> <img src={require('./../img/home_icon_2.png')} className="icon" />${textPrice}</a></div>
}
const Marker5 = ( {  textAddress, textPrice } ) => {
  return <div><a href="#" data-toggle="tooltip" title={textAddress}> <img src={require('./../img/home_icon_2.png')} className="icon" />${textPrice}</a></div>
}
const Marker6 = ( {  textAddress, textPrice } ) => {
  return <div><a href="#" data-toggle="tooltip" title={textAddress}> <img src={require('./../img/home_icon_2.png')} className="icon" />${textPrice}</a></div>
}

const Map = (props) => (

  // Important! Always set the container height explicitly
  <div className="map_comp">

    <GoogleMapReact
      bootstrapURLKeys={{ key: 'INSERT GOOGLE API KEY' }}
      center={{ lat: props.propDetails.lat, lng: props.propDetails.long }}
      defaultZoom={17}
    >
   
      {props.propDetails.comparables.length > 0 ? <Marker lat={props.propDetails.comparables[0].lat} lng={props.propDetails.comparables[0].long}  textAddress={props.propDetails.comparables[0].shortAddress} textPrice={props.propDetails.comparables[0].price}/>: null }
      {props.propDetails.comparables.length > 1 ? <Marker lat={props.propDetails.comparables[1].lat} lng={props.propDetails.comparables[1].long}  textAddress={props.propDetails.comparables[1].shortAddress} textPrice={props.propDetails.comparables[1].price}/>: null }
      {props.propDetails.comparables.length > 2 ? <Marker lat={props.propDetails.comparables[2].lat} lng={props.propDetails.comparables[2].long}  textAddress={props.propDetails.comparables[2].shortAddress} textPrice={props.propDetails.comparables[2].price}/>: null }
      {props.propDetails.comparables.length > 3 ? <Marker lat={props.propDetails.comparables[3].lat} lng={props.propDetails.comparables[3].long}  textAddress={props.propDetails.comparables[3].shortAddress} textPrice={props.propDetails.comparables[3].price}/>: null }
      {props.propDetails.comparables.length > 4 ? <Marker lat={props.propDetails.comparables[4].lat} lng={props.propDetails.comparables[4].long}  textAddress={props.propDetails.comparables[4].shortAddress} textPrice={props.propDetails.comparables[4].price}/>: null }
      {props.propDetails.comparables.length > 5 ? <Marker lat={props.propDetails.comparables[5].lat} lng={props.propDetails.comparables[5].long}  textAddress={props.propDetails.comparables[5].shortAddress} textPrice={props.propDetails.comparables[5].price}/>: null }
    </GoogleMapReact>
  </div>
);

export default connect(mapStateToProps)(Map);