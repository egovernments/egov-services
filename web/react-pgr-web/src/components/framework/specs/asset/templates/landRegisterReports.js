import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import Api from '../../../../../api/api';


var value= "" ;
var logo = "";
export default class assetImmovableReport extends Component {
	constructor(props) {
    super(props);
    this.state= {
		value: "",
		logo: ""

    }
  }

	componentDidMount() {
		var self = this;
		Api.commonApiPost("tenant/v1/tenant/_search", {code:localStorage.getItem("tenantId")?localStorage.getItem("tenantId"):'default'}).then(function(res){

			 self.setState({
				 value: res.tenant[0].city.name,
				 logo : res.tenant[0].logoId
			 })
			 console.log(value);
		}, function(err){
				console.log(err);
		})
  }



	convertToDate = (time) => {
		if (time) {
			let date = new Date(time);
			return ('0' + date.getDate()).slice(-2) + '/'
             + ('0' + (date.getMonth()+1)).slice(-2) + '/'
             + date.getFullYear();
		}
	}


	 numberWithCommas = (x)=> {
		if (x) {
			console.log(x);
			x=x.toString();
			var y = x.split(".")[1];
			x=x.split(".")[0];
			var lastThree = x.substring(x.length-3);
			var otherNumbers = x.substring(0,x.length-3);
			if(otherNumbers != '')
			    lastThree = ',' + lastThree;
			var resCal = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + lastThree ;
			var res = (y==null) ? resCal : (resCal + "." + y);
			return res;
		}
}


	render () {
		let {convertToDate,numberWithCommas}= this;
		return (
			<Card className="uiCard">
			    <CardHeader title="" />
			    <CardText>
			        <Table responsive bordered condensed>
			            <tbody>
			                <tr>
			                    <td  colSpan={2} rowSpan={3} style={{textAlign: "center"}}>
			                        <img src={this.state.logo} height="60" width="60" />
			                    </td>
			                    <td colSpan={8} style={{textAlign: "center"}} >
			                        <b>{this.state.value}</b>
			                    </td>
			                    <td  colSpan={2} rowSpan={3} style={{textAlign: "center"}}>
			                        <img src="./temp/images/AS.png" height="60" width="60" />
			                    </td>
			                </tr>
			                <tr>
			                    <td colSpan={8} style={{textAlign: "center"}} >
			                       <b>नमुना  क्रमांक  १६</b>
			                    </td>

			                </tr>
			                <tr>
			                    <td colSpan={8} style={{textAlign: "center"}} >
			                        <b>( नियम क्रमांक ६२ , १९१ पहा )</b>
			                    </td>
			                </tr>

			            </tbody>
									<tbody>
										<td  colSpan={12} style={{textAlign: "center"}}>
												<b>स्थावर  मालमत्तांची नोंदवही</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>मत्तेचे  नाव </b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{this.props.data[32]?this.props.data[32]:""}</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>जमिनीसह , इमारत , वृक्ष , कोणतेही असल्यास , संपादित केलेली /b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>मत्तेचा  ओळखपत्र क्रमांक</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{this.props.data[33]?this.props.data[33]:""}</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>इमारत , वृक्ष संपादित करण्याकरिता देण्यात आलेले मूल्य</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{"N/A"}</b>
										</td>
									</tbody>

									<tbody>
											<tr>
													<td colSpan={3} rowSpan={2} style={{textAlign: "left"}}>
															<b> करारनाम्याचा  संदर्भ  क्रमांक </b>
													</td>
													<td colSpan={3} rowSpan={2} style={{textAlign: "left"}} >
															<b>{"N/A"}</b>
													</td>
													<td colSpan={3} style={{textAlign: "left"}} >
														 <b>त्यावर स्थिरमत्ता नोंदवहीचा संदर्भ क्रमांक</b>
													</td>
													<td colSpan={3} style={{textAlign: "left"}} >
														 <b>{"NA"}</b>
													</td>
											</tr>
									</tbody>
                  <tbody>
									<td colSpan={3} style={{textAlign: "left"}}>
											<b>उद्देश</b>
									</td>
									<td colSpan={3} style={{textAlign: "left"}}>
											<b>{"NA"}</b>
									</td>
                  <td colSpan={3} style={{textAlign: "left"}}>
                      <b>राखून ठेवलेली प्रतिभूती ठेव</b>
                  </td>
                  <td colSpan={3} style={{textAlign: "right"}}>
                      <b>{numberWithCommas(this.props.data[16]?this.props.data[16]:"")}</b>
                  </td>
									</tbody>

									<tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>कोणाकडून संपादन केली</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{this.props.data[43]?this.props.data[43]:""}</b>
										</td>
                    <td colSpan={3} style={{textAlign: "left"}}>
												<b>वसूल केलेली प्रतिभूति ठेव</b>
										</td>
										<td colSpan={3} style={{textAlign: "right"}}>
												<b>{numberWithCommas(this.props.data[17]?this.props.data[17]:"")}</b>
										</td>
									</tbody>


									<tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>जमिनीचे क्षेत्र</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{"NA"}</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>दिनांक</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{convertToDate(this.props.data[18]?this.props.data[18]:"")}</b>
										</td>
									</tbody>
                  <tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>स्थान</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{"NA"}</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b> रक्कम रु.</b>
										</td>
										<td colSpan={3} style={{textAlign: "right"}}>
												<b>{numberWithCommas(this.props.data[21]?this.props.data[21]:"")}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>संपादनाची पद्धत</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{this.props.data[11]?this.props.data[11]:""}</b>
										</td>
										<td colSpan={6} style={{textAlign: "left"}}>
												<b>विक्री करणे</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>पट्ट्याने घेतली आहे / मालकीची आहे किंवा कसे</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{this.props.data[42]?this.props.data[42]:""}</b>
										</td>
										<td  colSpan={6} style={{textAlign: "center"}}>
												<b>ज्या व्यक्तिला विक्री करावयाचे त्या व्यक्तीचे नावे</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>जमिनीचा सर्वक्षण क्रमांक</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{"NA"}</b>
										</td>
									</tbody>
                  <tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>जमिनीच्या सीमा, रेखाचित्र</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{"NA"}</b>
										</td>
									</tbody>
                  <tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>निधीचे स्रोत</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{"NA"}</b>
										</td>
									</tbody>
                  <tbody>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>उपलब्ध शीर्ष दस्तऐवज</b>
										</td>
										<td colSpan={3} style={{textAlign: "left"}}>
												<b>{"NA"}</b>
										</td>
									</tbody>

									<tbody>
											<tr>
													<td rowSpan={3} style={{textAlign: "center"}}>
															<b>संपादन / सुधारणा केल्याचा दिनांक </b>
													</td>
													<td rowSpan={3} style={{textAlign: "center"}}>
															<b>संपादन / सुधारणा  करण्याचा</b>
													</td>
													<td rowSpan={3} style={{textAlign: "center"}}>
															<b>प्रमाणक  क्रमांक</b>
													</td>
													<td colSpan={4} style={{textAlign: "center"}} >
															<b>वजाती</b>
													</td>
													<td  rowSpan={2} colSpan={4} style={{textAlign: "center"}}>
															<b>पुनर्मुल्यांकन</b>
													</td>
													<td rowSpan={2} colSpan={3} style={{textAlign: "center"}}>
															<b>संचयित  घसारा</b>
													</td>
													<td rowSpan={3} style={{textAlign: "center"}}>
															<b>अंतिम डब्ल्यू  डी व्ही रु.</b>
													</td>
													<td rowSpan={3} style={{textAlign: "center"}}>
															<b>विभागप्रमुखाचा  शेरा ,सही</b>
													</td>
											</tr>
											<tr>
													<td colSpan={4} style={{textAlign: "center"}} >
														 <b>( हस्तांतरण  / विक्री करणे )</b>
													</td>

											</tr>
											<tr>
													<td style={{textAlign: "center"}} >
															<b>दिनांक </b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>संख्या / क्रमांक </b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>रक्कम रु.</b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>प्रमाणक क्रमांक</b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>दिनांक</b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>भर घालणे रु.</b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>वजा करणे रु. </b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>एकूण </b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>प्रारंभिक शिल्लक </b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>चालू वर्षाचा घसारा </b>
													</td>
													<td style={{textAlign: "center"}} >
															<b>अखेरची घसारा रक्कम</b>
													</td>
											</tr>
									</tbody>

									<tbody>
									<tr>
											<td style={{textAlign: "center"}} >
													<b>1</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>2</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>3</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>4</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>5</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>6</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>7</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>8</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>9</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>10</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>11</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>12</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>13</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>14</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>15</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>16</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>17</b>
											</td>
									</tr>
									</tbody>
									<tbody>
									<tr>
											<td style={{textAlign: "center"}} >
													<b>{convertToDate(this.props.data[19]?this.props.data[19]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[20]?this.props.data[20]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[21]?this.props.data[21]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>N/A</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{convertToDate(this.props.data[26]?this.props.data[26]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{this.props.data[1]?this.props.data[1]:""}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[24]?this.props.data[24]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{this.props.data[25]?this.props.data[25]:""}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{convertToDate(this.props.data[27]?this.props.data[27]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[28]?this.props.data[28]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[29]?this.props.data[29]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[23]?this.props.data[23]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[40]?this.props.data[40]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[30]?this.props.data[30]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[31]?this.props.data[31]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b>{numberWithCommas(this.props.data[23]?this.props.data[23]:"")}</b>
											</td>
											<td style={{textAlign: "center"}} >
													<b></b>
											</td>
									</tr>
									</tbody>
			        </Table>

			    </CardText>
			</Card>
		);
	}
}
