import React from 'react';
import Header from '../components/Header';

export default class CostCalculator extends React.Component {
    state = {
        mortgage_amount: "",
        interest_rate: "",
        mortgage_period: "",
        monthly_payments: "",
        total_cost: "",
        total_interest_paid: "",
        switch_option: false
    };

    handleChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        });
    };

    addCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    }

    updateSwitch = (e) => {
        if (e.target.className == 'switch-deselected') {
            this.setState({switch_option: !this.state.switch_option});
        }
    };

    calculate = (e) => {

        if (!this.state.switch_option) {
            const c_interest_rate = this.state.interest_rate;
            const c_mortgage_amount = this.state.mortgage_amount;
            const c_mortgage_period = this.state.mortgage_period;

            //Monthly payment = [ r + r / ( (1+r) ^ months -1) ] x principal loan amount
            //Where: r = decimal rate / 12

            const r = (parseFloat(c_interest_rate) / 100 / 12);
            const denom = Math.pow((r + 1), (parseFloat(c_mortgage_period) * 12)) - 1;
            const leftSide = (r + (r / denom));
            const monthlyPayment = (leftSide * parseFloat(c_mortgage_amount));
            const total_cost_ = monthlyPayment * (parseFloat(c_mortgage_period)) * 12;
            const total_interest_paid_ = total_cost_ - parseFloat(c_mortgage_amount);

            this.setState({
                monthly_payments: parseFloat(monthlyPayment).toFixed(2),
                total_cost: parseFloat(total_cost_).toFixed(2),
                total_interest_paid: parseFloat(total_interest_paid_).toFixed(2)
            });
        }
        else {
            const c_interest_rate = this.state.interest_rate;
            const c_mortgage_period = this.state.mortgage_period;
            const c_monthly_payment = this.state.monthly_payments;

            //Monthly payment / [ r + r / ( (1+r) ^ months -1) ] = principal loan amount
            //Where: r = decimal rate / 12

            const r = (parseFloat(c_interest_rate) / 100 / 12);
            const denom = Math.pow((r + 1), (parseFloat(c_mortgage_period) * 12)) - 1;
            const leftSide = (r + (r / denom));
            const loanAmount = (parseFloat(c_monthly_payment) / leftSide);
            const total_cost_ = c_monthly_payment * (parseFloat(c_mortgage_period)) * 12;
            const total_interest_paid_ = total_cost_ - parseFloat(loanAmount);

            this.setState({
                mortgage_amount: parseFloat(loanAmount).toFixed(2),
                total_cost: parseFloat(total_cost_).toFixed(2),
                total_interest_paid: parseFloat(total_interest_paid_).toFixed(2)
            });

        }

        e.preventDefault();
    }

    render() {
        return (
            <div>
                <Header />
            <div className="container-fluid cc-background">
                <div className="row">
                    <div className="col-md-3 col-sm-6 col-xs-6 col-md-offset-3 easycenter">
                        <div id="monthlycost-switch" className={this.state.switch_option ? "switch-deselected" : "switch-selected"} onClick={this.updateSwitch}>Monthly Cost</div>
                    </div>
                    <div className="col-md-3 col-sm-6 col-xs-6 easycenter">
                        <div id="maximumloan-switch" className={this.state.switch_option ? "switch-selected" : "switch-deselected"} onClick={this.updateSwitch}>Maximum Loan</div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-offset-2 col-sm-8 pt-20 easycenter description-text">
                        {this.state.switch_option ? "Enter your mortgage amount, interest rate, and mortgage period to calculate your monthly payment." : "Enter your desired monthly payments, interest rate, and mortgage period to calculate your maximum possible loan amount." }
                    </div>
                </div>
                <form className="calculationForm" onKeyUp={this.calculate}>
                    <div className="row">
                        <div className="col-md-4 cc-margin-row">
                            <label className="cc-margin-label">{this.state.switch_option ? "Monthly payments" : "Mortgage amount"}</label><br />
                            <input id="dynamicfield" pattern="\d*" type="text" value={this.state.switch_option ? this.state.monthly_payments : this.state.mortgage_amount} name={this.state.switch_option ? "monthly_payments" : "mortgage_amount"} onChange={this.handleChange} />
                        </div>
                        <div className="col-md-4 cc-margin-row">
                            <label className="cc-margin-label">Interest rate (%) </label><br />
                            <input type="text" pattern="\d*" value={this.state.interest_rate} name="interest_rate" onChange={this.handleChange} />
                        </div>
                        <div className="col-md-4 cc-margin-row">
                            <label className="cc-margin-label">Mortgage period (years) </label><br />
                            <input type="text" pattern="\d*" value={this.state.mortgage_period} name="mortgage_period" onChange={this.handleChange} />
                        </div>
                    </div>


                    <div className="row cc-margin-row">
                        <div className="col-sm-6">
                            <h4 className="cc-margin-label">Total Cost of Mortgage</h4>
                        </div>
                        <div className="col-sm-6">
                            <h4>${this.addCommas(this.state.total_cost)}</h4>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-sm-6">
                            <h4>Total Interest Paid</h4>
                        </div>
                        <div className="col-sm-6">
                            <h4>${this.addCommas(this.state.total_interest_paid)}</h4>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-sm-6">
                            <h4>{this.state.switch_option ? "Maximum loan" : "Monthly cost"}</h4>
                        </div>
                        <div className="col-sm-6">
                            <h4><strong>${this.addCommas(this.state.switch_option ? this.state.mortgage_amount : this.state.monthly_payments)}</strong></h4>
                        </div>
                    </div>
                </form>
            </div>
            </div>
        );
    };
}
