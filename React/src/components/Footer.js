import React from 'react';
import axios from 'axios';
import connect from 'react-redux/lib/connect/connect';

const mapStateToProps = (state) => {
    //console.log(state);
    return {
        propDetails: state.propDetails
    };
}

class Footer extends React.Component {  

    render () {
        return (
            <div>
                <footer>
                    <div className="footer-image"></div>
                    <div>
                        2018 Copyright CoreLogic. All Rights Reserved.
                    </div>
                    <div>
                        <ol>
                            <li><a href="https://www.corelogic.com/about-us/our-company.aspx">About Our Company</a></li>
                            <li><a href="https://www.corelogic.com/about-us/contact-us.aspx">Contact Us</a></li>
                            <li><a href="#">FAQs</a></li>
                            <li><a href="#">Sitemap</a></li>
                            <li><a href="#">Terms of Use</a></li>
                            <li><a href="#">Policies</a></li>
                        </ol>
                    </div>
                </footer>
            </div>
        );
    }
    
};

export default connect(mapStateToProps)(Footer);
