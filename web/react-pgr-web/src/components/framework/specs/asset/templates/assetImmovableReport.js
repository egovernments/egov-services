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
			                    <td  colSpan={1} rowSpan={3} style={{textAlign: "left"}}>
			                        <img src="./temp/images/headerLogo.png" height="60" width="60" />
			                    </td>
			                    <td colSpan={2} style={{textAlign: "center"}} >
			                        <b>PIMPRI CHINCHWAD MUNICIPAL CORPORATION</b>
			                    </td>
			                    <td  colSpan={1} rowSpan={3} style={{textAlign: "right"}}>
			                        <img src="./temp/images/AS.png" height="60" width="60" />
			                    </td>
			                </tr>
			                <tr>
			                    <td colSpan={2} style={{textAlign: "center"}} >
			                       <b>नमुना  क्रमांक  १६</b>
			                    </td>

			                </tr>
			                <tr>
			                    <td colSpan={2} style={{textAlign: "center"}} >
			                        <b>( नियम क्रमांक ६२ , १९१ पहा )</b>
			                    </td>
			                </tr>

			            </tbody>
									<tbody>
										<td  colSpan={4} style={{textAlign: "center"}}>
												<b>स्थावर  मालमत्तांची नोंदवही</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>मत्तेचे  नाव</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>Storm Water Drain</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>संपादनाची पद्धत</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>New</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>मत्तेचा  ओळखपत्र क्रमांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>ROH/SWD/IM/1234</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b> निधीचे स्रोत</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>Own</b>
										</td>
									</tbody>

									<tbody>
											<tr>
													<td colSpan={4} rowSpan={2} style={{textAlign: "left"}}>
															<b>मालमत्ता ज्या अन्वये संपादित केली होती</b>
													</td>
													<td colSpan={4} rowSpan={2} style={{textAlign: "center"}} >
															<b> GO/ROH/202 </b>
													</td>
													<td colSpan={5} style={{textAlign: "right"}}>
															<b>अधिपत्र ( होय / नाही )</b>
													</td>
													<td colSpan={4} style={{textAlign: "right"}}>
															<b>10 Years</b>
													</td>
											</tr>
											<tr>
													<td colSpan={5} style={{textAlign: "center"}} >
														 <b>असल्यास , समाप्ती दिनांक</b>
													</td>
													<td colSpan={4} style={{textAlign: "center"}} >
														 <b>05/10/2022</b>
													</td>
											</tr>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>स्थान</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>Gandhinagar</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>दोषी दायित्व</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>प्रगतिपथावरील बांधकाम नोंदवहीचा संदर्भ क्रमांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>राखून ठेवलेली प्रतिभूती ठेव</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>वसूल केलेली प्रतिभूति ठेव</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>5000000</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>मजल्यांची संख्या :</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>जोते क्षेत्र :</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b>दिनांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b> घनफळ</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b> रक्कम रु.</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b>310000000</b>
										</td>
									</tbody>

									<tbody>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b> ज्यावर बांधकाम केले आहे अशा जमिनीचा सर्वेक्षण क्रमांक</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
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
												<b>L:20Kms B:3mts H:2mts</b>
										</td>
										<td colSpan={5} style={{textAlign: "center"}}>
												<b> ज्यास मत्ता विकण्यात आली त्या व्यक्तीचे नाव (asset sold to)</b>
										</td>
										<td colSpan={4} style={{textAlign: "center"}}>
												<b></b>
										</td>
									</tbody>

									<tbody>
										<tr>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b>बांधण्यात आलेल्या जमिनीचे क्षेत्र</b>
											</td>
											<td colSpan={4} style={{textAlign: "center"}}>
													<b></b>
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
													<b>Yes</b>
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
													<b>600 Sq.Meters</b>
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
													<b>25 Years</b>
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
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
									<td style={{textAlign: "center"}} >
											<b>Data</b>
									</td>
							</tr>
							</tbody>
			        </Table>
			    </CardText>
			</Card>
		);
	}
}
