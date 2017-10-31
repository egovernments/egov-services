import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';

export default class assetImmovableReport extends Component {
	render () {
		return (
			<Card className="uiCard">
			    <CardHeader title="Certificate" />
			    <CardText>
			        <Table responsive bordered condensed>
			            <tbody>
			                <tr>
			                    <td  colSpan={4} rowSpan={3} style={{textAlign: "left"}}>
			                        <img src="./temp/images/headerLogo.png" height="60" width="60" />
			                    </td>
			                    <td colSpan={5} style={{textAlign: "center"}} >
			                        <b>PIMPRI CHINCHWAD MUNICIPAL CORPORATION</b>
			                    </td>
			                    <td  colSpan={5} rowSpan={3} style={{textAlign: "right"}}>
			                        <img src="./temp/images/AS.png" height="60" width="60" />
			                    </td>
			                </tr>
			                <tr>
			                    <td colSpan={5} style={{textAlign: "center"}} >
			                       <b>नमुना  क्रमांक  १६</b>
			                    </td>

			                </tr>
			                <tr>
			                    <td colSpan={5} style={{textAlign: "center"}} >
			                        <b>( नियम क्रमांक ६२ , १९१ पहा )</b>
			                    </td>
			                </tr>

			            </tbody>
									<tbody>
										<td  colSpan={17} style={{textAlign: "center"}}>
												<b>स्थावर  मालमत्तांची नोंदवही</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>मत्तेचे  नाव Name of Asset </b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>N/A</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>संपादनाची पद्धत</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[11]?this.props.data[11]:""}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>मत्तेचा  ओळखपत्र क्रमांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[0]?this.props.data[0]:""}</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b> निधीचे स्रोत</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>N/A</b>
										</td>
									</tbody>

									<tbody>
											<tr>
													<td colSpan={4} rowSpan={2} style={{textAlign: "center"}}>
															<b>मालमत्ता ज्या अन्वये संपादित केली होती, खरेदी केली होती किंवा बांधली होती,
 															त्या आदेशाचा क्रमांक व दिनांक</b>
													</td>
													<td colSpan={4} rowSpan={2} style={{textAlign: "center"}} >
															<b>{this.props.data[18]?this.props.data[18]:""}</b>
													</td>
													<td colSpan={5} style={{textAlign: "center"}}>
															<b>अधिपत्र ( होय / नाही )</b>
													</td>
													<td colSpan={4} style={{textAlign: "center"}}>
															<b>N/A</b>
													</td>
											</tr>
											<tr>
													<td colSpan={5} style={{textAlign: "center"}} >
														 <b>असल्यास , समाप्ती दिनांक</b>
													</td>
													<td colSpan={4} style={{textAlign: "center"}} >
														 <b>{this.props.data[12]?this.props.data[12]:""}</b>
													</td>
											</tr>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>स्थान</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>N/A</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>दोषी दायित्व</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[13]?this.props.data[13]:""}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>प्रगतिपथावरील बांधकाम नोंदवहीचा संदर्भ क्रमांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>N/A</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>राखून ठेवलेली प्रतिभूती ठेव</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[16]?this.props.data[16]:""}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>मजल्यांची संख्या :</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>N/A</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>वसूल केलेली प्रतिभूति ठेव</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[17]?this.props.data[17]:""}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>जोते क्षेत्र :</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[2]?this.props.data[2]:""}</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>दिनांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[18]?this.props.data[18]:""}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b> घनफळ</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[3]?this.props.data[3]:""}</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b> रक्कम रु.</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[23]?this.props.data[23]:""}</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b> ज्यावर बांधकाम केले आहे अशा जमिनीचा सर्वेक्षण क्रमांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[4]?this.props.data[4]:""}</b>
										</td>
										<td  colSpan={9} style={{textAlign: "center"}}>
												<b>विक्री करणे</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b> संरचनेचा आकार</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>{this.props.data[5]?this.props.data[5]:""}</b>
												<b>{this.props.data[6]?this.props.data[6]:""}</b>
												<b>{this.props.data[7]?this.props.data[7]:""}</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b> ज्यास मत्ता विकण्यात आली त्या व्यक्तीचे नाव (asset sold to)</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>N/A</b>
										</td>
									</tbody>

									<tbody>
										<tr>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b>बांधण्यात आलेल्या जमिनीचे क्षेत्र</b>
											</td>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b>{this.props.data[8]?this.props.data[8]:""}</b>
											</td>
											<td rowSpan={4} colSpan={9} style={{textAlign: "center"}}>
													<b></b>
											</td>
										</tr>
										<tr>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b> उपलब्ध शीर्ष दस्तऐवज</b>
											</td>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b>N/A</b>
											</td>
											<td rowSpan={4} colSpan={9} style={{textAlign: "center"}}>
													<b></b>
											</td>
										</tr>
										<tr>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b>कोणाकडून संपादित केली</b>
											</td>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b>{this.props.data[9]?this.props.data[9]:""}</b>
											</td>
											<td rowSpan={4} colSpan={9} style={{textAlign: "center"}}>
													<b></b>
											</td>
										</tr>
										<tr>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b> मत्तेचे  अपेक्षित आयुर्मान</b>
											</td>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b>{this.props.data[10]?this.props.data[10]:""}</b>
											</td>
											<td rowSpan={4} colSpan={9} style={{textAlign: "center"}}>
													<b></b>
											</td>
										</tr>
									</tbody>
			        </Table>


			        <Table responsive bordered condensed>
							<tbody>
									<tr>
											<td rowSpan={3} style={{textAlign: "left"}}>
													<b>संपादन / बांधकाम / सुधारणा केल्याचा दिनांक</b>
											</td>
											<td rowSpan={3} style={{textAlign: "left"}}>
													<b>प्रारंभिक डब्ल्यू  डी व्ही रु.</b>
											</td>
											<td rowSpan={3} style={{textAlign: "left"}}>
													<b>संपादन / बांधकाम सुधारणाऱ्याचा  खर्च</b>
											</td>
											<td rowSpan={3} style={{textAlign: "left"}}>
													<b>प्रमाणक  क्रमांक</b>
											</td>
											<td colSpan={4} style={{textAlign: "center"}} >
													<b>वजाती</b>
											</td>
											<td  rowSpan={2} colSpan={4} style={{textAlign: "right"}}>
													<b>पुनर्मुल्यांकन</b>
											</td>
											<td rowSpan={2} colSpan={3} style={{textAlign: "right"}}>
													<b>संचयित  घसारा</b>
											</td>
											<td rowSpan={3} style={{textAlign: "right"}}>
													<b>अंतिम डब्ल्यू  डी व्ही रु.</b>
											</td>
											<td rowSpan={3} style={{textAlign: "right"}}>
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
											<b>{this.props.data[19]?this.props.data[19]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[20]?this.props.data[20]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>N/A</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>N/A</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[26]?this.props.data[26]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[1]?this.props.data[1]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[24]?this.props.data[24]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[25]?this.props.data[25]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[27]?this.props.data[27]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[28]?this.props.data[28]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[29]?this.props.data[29]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>N/A</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>N/A</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[30]?this.props.data[30]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[31]?this.props.data[31]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>{this.props.data[22]?this.props.data[22]:""}</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>N/A</b>
									</td>
							</tr>
							</tbody>
			        </Table>
			    </CardText>
			</Card>
		);
	}
}
