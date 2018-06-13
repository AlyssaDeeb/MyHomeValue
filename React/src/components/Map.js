import React from 'react';
import GoogleMapReact from 'google-map-react';
import { connect } from 'react-redux';

const mapStateToProps = (state) => {
  //console.log(state);
  return {
      propDetails: state.propDetails
     
  };
};

const Map = (props) => (  

      // Important! Always set the container height explicitly
      <div className="map">
      
        <GoogleMapReact
          bootstrapURLKeys={{ key: 'INSERT GOOGLE API KEY'}}
          center={{lat: props.propDetails.lat , lng: props.propDetails.long}}
          defaultZoom={12}
        >
       
   
        </GoogleMapReact>
      </div>  
  
);


export default connect(mapStateToProps)(Map);