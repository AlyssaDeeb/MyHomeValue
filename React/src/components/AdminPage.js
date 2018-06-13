import React from 'react';
import axios from 'axios';
import variables from '../config/config.json';

export default class CostCalculator extends React.Component {

    constructor(props) {
        super(props);
    }

    componentDidMount () {
    }

    state = {
        id: "",
    };

    handleChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        });
    };

    addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    }

    generatePDF() {

        const pdf = new jsPDF();
        pdf.setFontSize(28);
        pdf.text(76, 25, 'Search History');
        pdf.setFontSize(10);
        pdf.setTextColor(0, 0, 255);
        pdf.text(16, 45, 'User ID');
        pdf.text(40, 45, 'Property ID');
        pdf.text(76, 45, 'Datetime');
        pdf.text(112, 45, 'User ID');
        pdf.text(136, 45, 'Property ID');
        pdf.text(172, 45, 'Datetime');
        pdf.setTextColor(0, 0, 0);

        var url = variables.baseURL + "/PDFServlet";
            axios.get(url).then(response => {
                pdf.text(9, 50, response.data.substring(11, response.data.indexOf('", "data2')));
                pdf.text(105, 50, " "+response.data.substring(response.data.indexOf('", "data2')+14, response.data.indexOf('"}')));
                pdf.save('Report-' + new Date().toLocaleString() + '.pdf');
            }
        );
    }

    render() {
        return (
            <div>
                <div className="container-fluid">
                    <div className="row">
                        <div className="col-md-12 col-sm-12 col-xs-12 easycenter pt-50 pb-50">
                            <h1 className="pb-30">Admin Page</h1>
                            <button onClick={this.generatePDF} type="button" className="btn btn-primary btn-lg">Generate Report</button>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-12">

                            <h2 className="pt-10 pb-10">Customize</h2>

                            <h3 className="pt-10 pb-10">Color Scheme</h3>

                            <form className="pb-10" align="center">
                              <input className="Color1" type="color" name="Color1" defaultValue="#c4c4c4" />
                              <input className="Color2" type="color" name="Color2" defaultValue="#454545" />
                              <input className="Color3" type="color" name="Color3" defaultValue="#ff6633" />
                            </form>

                            <h3 className="pt-10 pb-10">Content</h3>

                            <form className="pb-10" className="Checkboxes">
                                <input type="checkbox" name="Neighborhood" defaultValue="Neighborhood" /> Neighborhood<br />
                                <input type="checkbox" name="Crime" defaultValue="Crime" /> Crime<br />
                                <input type="checkbox" name="Schools" defaultValue="Schools" /> Schools<br />
                                <input type="checkbox" name="Transportation" defaultValue="Transportation" /> Transportation<br />
                                <input type="checkbox" name="Nearby Homes" defaultValue="Nearby Homes" /> Nearby Homes<br />
                                <input type="checkbox" name="Cost Calculator" defaultValue="Cost Calculator" /> Cost Calculator<br />
                                <input type="checkbox" name="Improve Home Value" defaultValue="Improve Home Value" /> Improve Home Value<br />
                                <input type="checkbox" name="What's Nearby" defaultValue="What's Nearby" /> What&apos;s Nearby<br />
                                <input type="checkbox" name="Saved Homes" defaultValue="Saved Homes" /> Saved Homes<br />
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    };
}
