import React from 'react';

export default class RecommendedBlock extends React.Component {
    state = {
        roi: 1000,
        originalvalue: 1000000,
    };

    componentDidMount() {
        this.setState({roi: this.props.roi, originalvalue: this.props.improvedValue});
    }

    // formats string to add in commas in the currency format
    addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    }

    render () {
        return (
            <div>
                <div className="recommended-block">
                    <div className="recommended-block-title">{this.props.name}</div>
                    <img className="recommended-block-img" src={this.props.img} />
                </div>
                {this.props.basic=="true" ?
                "" :
                <div>
                    <div className={this.state.roi >= 0 ? "roi-text-green" : "roi-text-red"}><img src={this.state.roi >=0 ? require('./../img/uparrow.png') : require('./../img/downarrow.png')} className="arrow" />${this.addCommas(this.state.roi.toFixed(2).substring(this.state.roi.toString().indexOf('-')+1, this.state.roi.toString().length))} ({ this.addCommas(parseFloat(100*this.state.roi/(this.props.improvedValue - this.state.roi)).toFixed(2)) }&#37;)</div>
                </div>
                }
            </div>
        );
    }
};
