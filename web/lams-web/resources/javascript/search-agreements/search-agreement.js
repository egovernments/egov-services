var flag = 0;
class AgreementSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      agreements: [],
      advancedSearch: false,
      searchSet: {
        locality: "",
        agreementNumber: "",
        assetCategory: "",
        mobileNumber: "",
        allotteeName: "",
        revenueWard: "",
        electionWard: "",
        assetCode: "",
        tenderNumber: "",
        fromDate: "",
        toDate: "",
        acknowledgementNumber: "",
        shopNumber: "",
        shoppingComplexName: "",
        referenceNumber: "",
        tenantId
      },
      searchSet2: {
        agreementNumber: "",
        assetCategory: "",
        tenantId
      },
      isSearchClicked: false,
      assetCategories: [],
      locality: [],
      revenueWards: [],
      electionwards: [],
      modify: false,
      users: [],
      hideCollectTaxOption: true
    };

    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.handleMobileValidation = this.handleMobileValidation.bind(this);
    this.handleSelectChange = this.handleSelectChange.bind(this);
    this.prepareNotice = this.prepareNotice.bind(this);
  }

  handleMobileValidation(e) {
    if (e.target.value && !/[0-9]{10}/.test(e.target.value)) {
      e.target.setCustomValidity("Please enter 10 digits.");
    } else {
      e.target.setCustomValidity("");
    }
  }
  search(e) {
    e.preventDefault();
    var _this = this;
    let { advancedSearch } = this.state;
    try {
      var searchSet = Object.assign({}, this.state.searchSet);
      var searchSet2 = Object.assign({}, this.state.searchSet2);
      if (searchSet.allottee) delete searchSet.allotteeName;
      else delete searchSet.allottee;

      //call api call
      var agreements = {};

      if (!advancedSearch) {
        let tempRes = {};
        tempRes =
          commonApiPost("lams-services", "agreements", "_search", searchSet)
            .responseJSON["Agreements"] || [];
        flag = 1;
        if (tempRes && tempRes.length) {
          tempRes.map((item, i) => {
            if (item.status.toLowerCase() === "history") {
              delete tempRes[i];
            }
          });
        }
        agreements = tempRes;
        if (agreements && agreements.length) {
          agreements.sort(function(d1, d2) {
            var date1 = d1.createdDate.split("/");
            var date2 = d2.createdDate.split("/");
            if (
              new Date(date1[2], date1[1] - 1, date1[0]).getTime() >
              new Date(date2[2], date2[1] - 1, date2[0]).getTime()
            ) {
              return -1;
            } else if (
              new Date(date1[2], date1[1] - 1, date1[0]).getTime() <
              new Date(date2[2], date2[1] - 1, date2[0]).getTime()
            ) {
              return 1;
            } else return 0;
          });
        }
      } else {
        agreements =
          commonApiPost(
            "lams-services",
            "agreements",
            "_advancesearch",
            searchSet2
          ).responseJSON["Agreements"] || [];
        flag = 1;
        if (agreements && agreements.length) {
          agreements.sort(function(d1, d2) {
            var date1 = d1.createdDate.split("/");
            var date2 = d2.createdDate.split("/");
            if (
              new Date(date1[2], date1[1] - 1, date1[0]).getTime() >
              new Date(date2[2], date2[1] - 1, date2[0]).getTime()
            ) {
              return -1;
            } else if (
              new Date(date1[2], date1[1] - 1, date1[0]).getTime() <
              new Date(date2[2], date2[1] - 1, date2[0]).getTime()
            ) {
              return 1;
            } else return 0;
          });
        }
      }

      _this.setState({
        isSearchClicked: true,
        agreements,
        modify: true
      });
      setTimeout(function() {
        _this.setState({
          modify: false
        });
      }, 1200);
    } catch (e) {
      console.log(e);
    }
  }

  componentWillMount() {
    try {
      var locality =
        !localStorage.getItem("locality") ||
        localStorage.getItem("locality") == "undefined"
          ? (localStorage.setItem(
              "locality",
              JSON.stringify(
                commonApiPost(
                  "egov-location/boundarys",
                  "boundariesByBndryTypeNameAndHierarchyTypeName",
                  "",
                  {
                    boundaryTypeName: "LOCALITY",
                    hierarchyTypeName: "LOCATION",
                    tenantId
                  }
                ).responseJSON["Boundary"] || []
              )
            ),
            JSON.parse(localStorage.getItem("locality")))
          : JSON.parse(localStorage.getItem("locality"));
    } catch (e) {
      console.log(e);
      var locality = [];
    }

    try {
      var electionwards =
        !localStorage.getItem("ward") ||
        localStorage.getItem("ward") == "undefined"
          ? (localStorage.setItem(
              "ward",
              JSON.stringify(
                commonApiPost(
                  "egov-location/boundarys",
                  "boundariesByBndryTypeNameAndHierarchyTypeName",
                  "",
                  {
                    boundaryTypeName: "WARD",
                    hierarchyTypeName: "ADMINISTRATION",
                    tenantId
                  }
                ).responseJSON["Boundary"] || []
              )
            ),
            JSON.parse(localStorage.getItem("ward")))
          : JSON.parse(localStorage.getItem("ward"));
    } catch (e) {
      console.log(e);
      var electionwards = [];
    }

    try {
      var revenueWards =
        !localStorage.getItem("revenueWard") ||
        localStorage.getItem("revenueWard") == "undefined"
          ? (localStorage.setItem(
              "revenueWard",
              JSON.stringify(
                commonApiPost(
                  "egov-location/boundarys",
                  "boundariesByBndryTypeNameAndHierarchyTypeName",
                  "",
                  {
                    boundaryTypeName: "WARD",
                    hierarchyTypeName: "REVENUE",
                    tenantId
                  }
                ).responseJSON["Boundary"] || []
              )
            ),
            JSON.parse(localStorage.getItem("revenueWard")))
          : JSON.parse(localStorage.getItem("revenueWard"));
    } catch (e) {
      console.log(e);
      var revenueWards = [];
    }

    try {
      var assetCategories =
        !localStorage.getItem("assetCategories") ||
        localStorage.getItem("assetCategories") == "undefined"
          ? (localStorage.setItem(
              "assetCategories",
              JSON.stringify(
                commonApiPost("asset-services", "assetCategories", "_search", {
                  usedForLease: true,
                  tenantId
                }).responseJSON["AssetCategory"] || []
              )
            ),
            JSON.parse(localStorage.getItem("assetCategories")))
          : JSON.parse(localStorage.getItem("assetCategories"));
    } catch (e) {
      console.log(e);
      var assetCategories = [];
    }

    var res = commonApiPost("asset-services", "assetCategories", "_search", {
      tenantId
    });
    var bool = true;

    if (res && res.getResponseHeader("userInfo")) {
      try {
        var roles = JSON.parse(res.getResponseHeader("userInfo")).roles;
        for (var i = 0; i < roles.length; i++) {
          if (roles[i].name == "Collection Operator") {
            bool = false;
            break;
          }
        }
      } catch (e) {
        console.log(e);
      }
    }
    /*if(res && res.responseJSON && res.responseJSON.Employee && res.responseJSON.Employee[0]) {
          var res2 = commonApiPost("hr-employee", "employees/" + res.responseJSON.Employee[0].id, "_search", {tenantId});
          if(res2 && res2.responseJSON && res2.responseJSON.Employee && res2.responseJSON.Employee.user && res2.responseJSON.Employee.user.roles) {
            for(var i=0; i<res2.responseJSON.Employee.user.roles.length; i++) {
              if(res2.responseJSON.Employee.user.roles[i].name == "Collection Operator") {
                bool = true;
                break;
              }
            }
          }
        }*/

    this.setState({
      assetCategories,
      locality,
      electionwards,
      revenueWards,
      hideCollectTaxOption: bool
    });
  }

  componentDidMount() {
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName(
        "homepage_logo"
      );
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src =
          window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

    let _this = this;

    //Fetch allottee name suggestions
    $("#name").autocomplete({
      source: function(request, response) {
        $.ajax({
          url: baseUrl + "/user/_search?tenantId=" + tenantId,
          type: "POST",
          dataType: "json",
          data: JSON.stringify({
            RequestInfo: requestInfo,
            name: request.term,
            fuzzyLogic: true,
            tenantId: tenantId
          }),
          contentType: "application/json",
          success: function(data) {
            if (data && data.user && data.user.length) {
              let users = [];
              for (let i = 0; i < data.user.length; i++)
                users.push(data.user[i].name);
              response(users);
              _this.setState({
                users: data.user
              });
            }
          }
        });
      },
      minLength: 3,
      change: function(event, ui) {
        if (ui.item && ui.item.value) {
          var id;
          if (_this.state.users && _this.state.users.constructor == Array) {
            for (var i = 0; i < _this.state.users.length; i++) {
              if (_this.state.users[i].name == ui.item.value) {
                id = _this.state.users[i].id;
              }
            }
          }

          _this.setState({
            searchSet: {
              ..._this.state.searchSet,
              allotteeName: ui.item.value,
              allottee: id || ""
            }
          });
        }
      }
    });

    $(".date-picker").datepicker({
      format: "dd/mm/yyyy",
      autoclose: true
    });

    $(".date-picker").on("change", function(e) {
      _this.setState({
        searchSet: {
          ..._this.state.searchSet,
          [e.target.id]: e.target.value
        }
      });
      if (
        _this.state.searchSet.fromDate != "" &&
        _this.state.searchSet.toDate == ""
      ) {
        $("#toDate").prop("required", true);
      } else if (
        _this.state.searchSet.fromDate == "" &&
        _this.state.searchSet.toDate != ""
      ) {
        $("#fromDate").prop("required", true);
      } else {
        $("#toDate").prop("required", false);
        $("#fromDate").prop("required", false);
      }
    });
  }

  componentWillUpdate() {
    if (flag == 1) {
      flag = 0;
      $("#agreementTable")
        .dataTable()
        .fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.state.modify) {
      $("#agreementTable").DataTable({
        dom: "Bfrtip",
        buttons: ["copy", "csv", "excel", "pdf", "print"],
        ordering: false,
        bDestroy: true,
        language: {
          emptyTable: "No Records"
        },
        columnDefs: [{ orderable: false, targets: 0 }]
      });
    }
  }

  handleChange(e, name) {
    if (name == "mobileNumber") {
      if (/[^0-9]/.test(e.target.value)) {
        return this.setState({
          searchSet: {
            ...this.state.searchSet,
            [name]: e.target.value.substring(0, e.target.value.length - 1)
          }
        });
      }
    }
    if (name === "advancedSearch") {
      if (e.target.checked) {
        $("#agreement_number_label").html("AgreementNumber<span> *</span>");
        $(
          "#shop_number_field,#old_agreement_number_field,#collapse_search_fields"
        ).prop("hidden", true);
        $("#demo").prop("class", "collapse");
        $("#agreement_number").prop("required", true);
      } else {
        $("#agreement_number_label").html("AgreementNumber");
        $("#agreement_number").prop("required", false);
        $(
          "#shop_number_field,#old_agreement_number_field,#collapse_search_fields"
        ).prop("hidden", false);
        //$("#demo").prop("class", "collapse in");
      }
      this.setState({
        ...this.state.searchSet,
        advancedSearch: e.target.checked
      });
    }

    if (name == "allotteeName") {
      return this.setState({
        searchSet: {
          ...this.state.searchSet,
          [name]: e.target.value,
          allottee: ""
        }
      });
    }

    if (name !== "advancedSearch") {
      this.setState({
        searchSet: {
          ...this.state.searchSet,
          [name]: e.target.value
        }
      });
    }
    console.log(name);
    if (name === "agreementNumber" || name === "assetCategory") {
      console.log(name);
      this.setState({
        searchSet2: {
          ...this.state.searchSet2,
          [name]: e.target.value
        }
      });
    }
  }

  handleSelectChange(
    type,
    id,
    number,
    action,
    agreementId,
    assetCategory,
    acknowledgementNumber,
    status
  ) {
    switch (type) {
      case "renew":
        window.open(
          "app/search-agreement/view-renew-agreement.html?view=renew&type=" +
            assetCategory +
            (number
              ? "&agreementNumber=" + number
              : "&acknowledgementNumber=" + acknowledgementNumber) +
            "&assetId=" +
            id,
          "fs",
          "fullscreen=yes"
        );
        break;
        case "collTax":

        var data = commonApiPost("lams-services", "payment", "_partialpaymentview", {agreementNumber:number, tenantId});
        let demandDetails = data.responseJSON.legacyDemands && data.responseJSON.legacyDemands[0].demandDetails? data.responseJSON.legacyDemands[0].demandDetails.length : 0;
        if(demandDetails > 0)
        window.open(
          "app/search-agreement/partial-payment.html?" +
            (number
              ? "&agreementNumber=" + number
              : "&acknowledgementNumber=" + acknowledgementNumber) +
            (status ? "&status=" + status : "") +
            (action ? "&action=" + action : "") +
            (agreementId ? "&id=" + agreementId : "") +
            "&assetId=" +
            id,
          "fs",
          "fullscreen=yes"
        )
        else
        return showError("Rent is fully paid or no demand details found for collection");
        // $.ajax({
        //   url:
        //     "/lams-services/payment/_partialpaymentview?tenantId=" +
        //     tenantId +
        //     "&" +
        //     (acknowledgementNumber
        //       ? "acknowledgementNumber=" + acknowledgementNumber
        //       : "agreementNumber=" + number),
        //   type: "POST",
        //   contentType: "application/json",
        //   data: JSON.stringify({
        //     RequestInfo: requestInfo
        //   }),
        //   success: function(data) {
        //     window.open(
        //       "app/search-agreement/partial-payment.html?" +
        //         (number
        //           ? "&agreementNumber=" + number
        //           : "&acknowledgementNumber=" + acknowledgementNumber) +
        //         (status ? "&status=" + status : "") +
        //         (action ? "&action=" + action : "") +
        //         (agreementId ? "&id=" + agreementId : "") +
        //         "&assetId=" +
        //         id,
        //       "fs",
        //       "fullscreen=yes"
        //     );
        //   },
        //   error: function(data) {
        //     console.log(data);
        //     showError(
        //       data.responseJSON.error
        //         ? data.responseJSON.error.message
        //         : data.responseJSON.message
        //     );
        //   }
        // });
        break;
        case "view":
        window.open(
          "app/search-agreement/view-agreement-details.html?" +
            (number
              ? "&agreementNumber=" + number
              : "&acknowledgementNumber=" + acknowledgementNumber) +
            (status ? "&status=" + status : "") +
            (action ? "&action=" + action : "") +
            (agreementId ? "&id=" + agreementId : "") +
            "&assetId=" +
            id,
          "fs",
          "fullscreen=yes"
        );
        break;
      case "cancel":
        window.open(
          "app/search-agreement/view-renew-agreement.html?view=cancel&type=" +
            assetCategory +
            (number
              ? "&agreementNumber=" + number
              : "&acknowledgementNumber=" + acknowledgementNumber) +
            "&assetId=" +
            id,
          "fs",
          "fullscreen=yes"
        );
        break;
      case "eviction":
        window.open(
          "app/search-agreement/view-renew-agreement.html?view=eviction&type=" +
            assetCategory +
            (number
              ? "&agreementNumber=" + number
              : "&acknowledgementNumber=" + acknowledgementNumber) +
            "&assetId=" +
            id,
          "fs",
          "fullscreen=yes"
        );
        break;
      case "addeditdemand":
        window.open(
          "app/dataentry/edit-demand.html?" +
            (number
              ? "agreementNumber=" + number
              : "acknowledgementNumber=" + acknowledgementNumber) +
            "&assetId=" +
            id,
          "fs",
          "fullscreen=yes"
        );
        break;

      case "generateNotice":
        var agreementNumber = number;
        var noticeType = "CREATE";
        var notice = commonApiPost(
          "lams-services/agreement",
          "notice",
          "_search",
          { tenantId, agreementNumber, noticeType }
        ).responseJSON.Notices[0];
        if (notice && notice.fileStore) {
          console.log(notice.fileStore);
          this.showNotice(notice.fileStore);
        } else {
          var status = "ACTIVE";
          var agreement = commonApiPost(
            "lams-services",
            "agreements",
            "_search",
            { tenantId, agreementNumber, status }
          ).responseJSON.Agreements[0];
          this.prepareNotice(agreement);
        }

        break;

      case "showNotice":
        var agreementNumber = number;
        var noticeType = "CREATE";
        var notice = commonApiPost(
          "lams-services/agreement",
          "notice",
          "_search",
          { tenantId, agreementNumber, noticeType }
        ).responseJSON.Notices[0];
        if (notice && notice.fileStore) {
          console.log(notice.fileStore);
          this.showNotice(notice.fileStore);
        } else {
          showError("No notice available");
        }
        break;
    }
  }
  prepareNotice(agreement) {
    debugger;
    var doc = new jsPDF();
    var tenderDate;
    if (agreement.tenderDate === null) {
      tenderDate = "N/A";
    } else {
      tenderDate = agreement.tenderDate;
    }
    var commDesignation = commonApiPost(
      "hr-masters",
      "designations",
      "_search",
      { name: "Commissioner", active: true, tenantId }
    ).responseJSON["Designation"];
    var commDesignationId = commDesignation[0].id;
    var commissioners =
      commonApiPost("hr-employee", "employees", "_search", {
        tenantId,
        designationId: commDesignationId,
        active: true,
        asOnDate: moment(new Date()).format("DD/MM/YYYY")
      }).responseJSON["Employee"] || [];
    var commissionerName = commissioners[0].name;
    var LocalityData = commonApiPost(
      "egov-location/boundarys",
      "boundariesByBndryTypeNameAndHierarchyTypeName",
      "",
      { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }
    );
    var locality = getNameById(
      LocalityData["responseJSON"]["Boundary"],
      agreement.asset.locationDetails.locality
    );
    var cityGrade =
      !localStorage.getItem("city_grade") ||
      localStorage.getItem("city_grade") == "undefined"
        ? (localStorage.setItem(
            "city_grade",
            JSON.stringify(
              commonApiPost("tenant", "v1/tenant", "_search", {
                code: tenantId
              }).responseJSON["tenant"][0]["city"]["ulbGrade"] || {}
            )
          ),
          JSON.parse(localStorage.getItem("city_grade")))
        : JSON.parse(localStorage.getItem("city_grade"));
    var ulbType = "Nagara Panchayat/Municipality";
    if (cityGrade.toLowerCase() === "corp") {
      ulbType = "Municipal Corporation";
    }

    let commencementDate = agreement.commencementDate;
    let timePeriod = agreement.timePeriod;
    let endDate =
      commencementDate.split("/")[0] +
      "/" +
      commencementDate.split("/")[1] +
      "/" +
      (Number(commencementDate.split("/")[2]) + Number(timePeriod));

    var depositParticulars = [
      { title: "Particulars", dataKey: "particulars" },
      { title: "Amount (Rs)", dataKey: "amount" },
      { title: "Cheque/DD/Challan No and Date", dataKey: "leaseHolderName" }
    ];
    var gstOnGoodWill = Number((agreement.goodWillAmount * 18) / 100);
    var gstOnAdvance = Number((agreement.securityDeposit * 18) / 100);
    var depositDetails = [
      {
        particulars: "Advance Tax(3 months Rental Deposits)",
        amount: agreement.securityDeposit,
        leaseHolderName: agreement.name
      },
      {
        particulars: "GST for Advance Tax",
        amount: gstOnAdvance,
        leaseHolderName: agreement.name
      },
      {
        particulars: "Goodwill Amount",
        amount: agreement.goodWillAmount,
        leaseHolderName: agreement.name
      },
      {
        particulars: "GST for Goodwill Amount",
        amount: gstOnGoodWill,
        leaseHolderName: agreement.name
      },
      {
        particulars: "Total",
        amount:
          Number(agreement.goodWillAmount) +
          Number(gstOnGoodWill) +
          Number(agreement.securityDeposit) +
          gstOnAdvance,
        leaseHolderName: agreement.name
      }
    ];

    var depositTableStyle = {
      tableLineColor: [0, 0, 0],
      tableLineWidth: 0.2,
      styles: {
        lineColor: [0, 0, 0],
        lineWidth: 0.2
      },
      headerStyles: {
        textColor: [0, 0, 0],
        fillColor: [255, 255, 255],
        overflow: "linebreak",
        columnWidth: "wrap"
      },
      bodyStyles: {
        fillColor: [255, 255, 255],
        textColor: [0, 0, 0],
        overflow: "linebreak",
        columnWidth: "wrap"
      },
      alternateRowStyles: {
        fillColor: [255, 255, 255]
      },
      startY: 135
    };

    var rentParticulars = [
      { title: "Particulars", dataKey: "particulars" },
      { title: "Amount (Rs)", dataKey: "amount" }
    ];

    var rentDetails = [
      { particulars: "Monthly Rental", amount: agreement.rent },
      {
        particulars: "GST",
        amount: Number(agreement.sgst) + Number(agreement.cgst)
      }
    ];

    var rentTableStyle = {
      tableLineColor: [0, 0, 0],
      tableLineWidth: 0.2,
      styles: {
        lineColor: [0, 0, 0],
        lineWidth: 0.2
      },
      headerStyles: {
        textColor: [0, 0, 0],
        fillColor: [255, 255, 255],
        overflow: "linebreak",
        columnWidth: "wrap"
      },
      bodyStyles: {
        fillColor: [255, 255, 255],
        textColor: [0, 0, 0],
        overflow: "linebreak",
        columnWidth: "wrap"
      },
      alternateRowStyles: {
        fillColor: [255, 255, 255]
      },
      startY: 200
    };

    doc.setFontType("bold");
    doc.setFontSize(12);
    doc.text(
      105,
      20,
      "PROCEEDINGS OF THE COMMISSIONER, " +
        tenantId.split(".")[1].toUpperCase(),
      "center"
    );
    doc.text(105, 27, ulbType.toUpperCase(), "center");
    doc.text(105, 34, "Present: " + commissionerName, "center");

    doc.setFontType("normal");
    doc.setFontSize(11);
    doc.text(15, 50, "Roc.No.");
    doc.setFontType("bold");
    doc.text(30, 50, agreement.noticeNumber ? agreement.noticeNumber : "");
    doc.setFontType("normal");
    doc.text(140, 50, "Dt. ");
    doc.setFontType("bold");
    doc.text(146, 50, agreement.agreementDate);

    doc.fromHTML(
      "Sub:Leases-Revenue Section-Shop No <b>" +
        agreement.referenceNumber +
        "</b> in <b>" +
        agreement.asset.name +
        "</b> Complex, <b> " +
        locality +
        "</b> <br>-Allotment of lease - orders - Issued",
      15,
      60
    );

    doc.fromHTML(
      "Ref: 1. Open Auction Notice dt. <b>" +
        tenderDate +
        "</b> of this office",
      15,
      80
    );

    doc.fromHTML(
      "2. Resolution No <b>" +
        agreement.councilNumber +
        "</b> dt <b>" +
        agreement.councilDate +
        "</b> of Municipal Council/Standing Committee",
      23,
      85
    );

    doc.text(100, 98, "><><><", "center");

    doc.text(15, 105, "Orders:");
    doc.setLineWidth(0.5);
    doc.line(15, 106, 28, 106);

    doc.fromHTML(
      "In the reference 1st cited, an Open Auction for leasing Shop No <b>" +
        agreement.referenceNumber +
        "</b> in <b>" +
        agreement.asset.name +
        "</b> <br>Complex was conducted and your bid for the highest amount (i.e. monthly rentals of Rs <b>" +
        agreement.rent +
        "/- </b> <br>and Goodwill amount of Rs <b>" +
        agreement.goodWillAmount +
        "/-  </b> ) was accepted by the Municipal Council/Standing Committee vide <br>reference 2nd cited with the following deposit amounts as received by this office.",
      15,
      100
    );

    doc.autoTable(depositParticulars, depositDetails, depositTableStyle);

    doc.fromHTML(
      "In pursuance of the Municipal Council/Standing Committee resolution and vide GO MS No 56 (MA & UD <br> Department) dt. 05.02.2011, the said shop is allotted to you for the period <b>" +
        commencementDate +
        "</b> to <b>" +
        endDate +
        "</b> at <br> following rates of rentals and taxes thereon.",
      15,
      176
    );

    doc.autoTable(rentParticulars, rentDetails, rentTableStyle);

    doc.setFontType("normal");
    var termsAndConditions =
      "The following terms and conditions are applicable for the renewal of lease." +
      "\n\t1. The leaseholder shall pay rent by 5th of the succeeding month" +
      "\n\t2. All the late payments of rentals will attract penalty and interest as applicable" +
      "\n\t3. The leaseholder shall not sub lease the premises in any case. If it is found that the premises are being \t    sub let to any person, the lease shall stand cancelled without any prior notice." +
      "\n\t4. The D&O Trade License of the establishment shall be in the name of the leaseholder only." +
      "\n\t5. The leaseholder shall do business in the name of himself only." +
      "\n\t6. The leaseholder not to use the premises for any unlawful activities" +
      "\n\t7. The Goodwill and the Rental Deposits paid by the leaseholder shall be forfeited in the event of violation \t    of terms and conditions in the agreement.";

    var lines = doc.splitTextToSize(termsAndConditions, 183);
    doc.text(12, 228, lines);

    doc.addPage();
    var declaration =
      "Hence you are requested to conclude an agreement duly registered with the SRO for the above mentioned lease within 15 days of receipt of this allotment letter without fail unless the allotment will stand cancelled without any further correspondence.";
    var lines = doc.splitTextToSize(declaration, 183);
    doc.text(15, 30, lines);

    doc.text(120, 50, "Commissioner");
    doc.fromHTML(
      "<b> " +
        tenantId
          .split(".")[1]
          .charAt(0)
          .toUpperCase() +
        tenantId.split(".")[1].slice(1) +
        " " +
        ulbType +
        "</b>",
      120,
      55
    );

    doc.text(15, 60, "To");
    doc.text(15, 65, "The Leaseholder");
    doc.text(15, 70, "Copy to the concerned officials for necessary action");

    var blob = doc.output("blob");

    this.createFileStore(agreement, blob).then(
      this.createNotice,
      this.errorHandler
    );
  }

  errorHandler(statusCode) {
    console.log("failed with status", status);
    showError("Error");
  }

  createFileStore(noticeData, blob) {
    var promiseObj = new Promise(function(resolve, reject) {
      let formData = new FormData();
      let fileName = "AN/" + noticeData.agreementNumber;
      formData.append("module", "LAMS");
      formData.append("file", blob, fileName);
      $.ajax({
        url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        type: "POST",
        success: function(res) {
          let obj = {
            noticeData: noticeData,
            fileStoreId: res.files[0].fileStoreId
          };
          resolve(obj);
        },
        error: function(jqXHR, exception) {
          reject(jqXHR.status);
        }
      });
    });
    return promiseObj;
  }

  createNotice(obj) {
    var CONST_API_GET_FILE =
      "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
    var filestore = obj.fileStoreId;
    $.ajax({
      url:
        baseUrl +
        `/lams-services/agreement/notice/_create?tenantId=` +
        tenantId,
      type: "POST",
      dataType: "json",
      data: JSON.stringify({
        RequestInfo: requestInfo,
        Notice: {
          tenantId,
          agreementNumber: obj.noticeData.agreementNumber,
          fileStore: obj.fileStoreId
        }
      }),
      headers: {
        "auth-token": authToken
      },
      contentType: "application/json",
      success: function(res) {
        if (window.opener)
          window.open(
            window.location.origin + CONST_API_GET_FILE + filestore,
            "_self"
          );
      },
      error: function(jqXHR, exception) {
        console.log("error");
        showError("Error while creating notice");
      }
    });
  }

  showNotice(fileStoreId) {
    let self = this;
    var fileURL =
      "/filestore/v1/files/id?fileStoreId=" +
      fileStoreId +
      "&tenantId=" +
      tenantId;
    var oReq = new XMLHttpRequest();
    oReq.open("GET", fileURL, true);
    oReq.responseType = "arraybuffer";
    console.log(fileURL);
    oReq.onload = function(oEvent) {
      var blob = new Blob([oReq.response], {
        type: oReq.getResponseHeader("content-type")
      });
      var url = URL.createObjectURL(blob);
      self.setState({
        iframe_src: url,
        contentType: oReq.getResponseHeader("content-type")
      });
      $(self.refs.modal).modal("show");
    };
    oReq.send();
  }

  close() {
    // widow.close();
    open(location, "_self").close();
  }

  render() {
    let {
      handleChangeFile,
      handleChange,
      prepareNotice,
      search,
      updateTable,
      handleSelectChange,
      handleMobileValidation
    } = this;
    var self = this;
    let {
      isSearchClicked,
      agreements,
      assetCategories,
      hideCollectTaxOption,
      advancedSearch
    } = this.state;
    let {
      locality,
      agreementNumber,
      shopNumber,
      shoppingComplexName,
      assetCategory,
      mobileNumber,
      allotteeName,
      revenueWard,
      electionWard,
      code,
      oldAgreementNumber,
      tenderNumber,
      fromDate,
      toDate,
      shopComplexNumber,
      acknowledgementNumber,
      referenceNumber
    } = this.state.searchSet;
    const showCollectTaxOption = function(item) {
      if (
        !hideCollectTaxOption &&
        !(item.action == "CREATE" && item.status == "REJECTED")
      ) {
        return (<option value="collTax">Collect Tax</option>);
      }
    };

    const getValueByName = function(name, id) {
      for (var i = 0; i < assetCategories.length; i++) {
        if (assetCategories[i].id == id) {
          return assetCategories[i][name];
        }
      }
    };

    const renderOption = function(list) {
      if (list) {
        return list.map(item => {
          return (
            <option key={item.id} value={item.id}>
              {item.name}
            </option>
          );
        });
      }
    };
    const showTable = function() {
      if (isSearchClicked) {
        return (
          <table id="agreementTable" className="table table-bordered">
            <thead>
              <tr>
                <th>Sl No </th>
                <th>Agreement Number </th>
                <th>Allottee Name </th>
                <th>Allottee contact No </th>
                <th>Locality </th>
                <th>Asset Category </th>
                <th>Asset Code </th>
                <th>Agreement Created Date </th>
                <th>Agreement Type</th>
                <th>Status </th>
                <th>Type</th>
                <th>Action </th>
              </tr>
            </thead>

            <tbody id="agreementSearchResultTableBody">{renderBody()}</tbody>
          </table>
        );
      }
    };
    const renderBody = () => {
      if (agreements.length > 0) {
        return agreements.map((item, index) => {
          var category_name = getValueByName(
            "name",
            item.asset.assetCategory.id
          );

          return (
            <tr key={index}>
              <td>{index + 1}</td>
              <td>{item.agreementNumber} </td>
              <td>{item.allottee.name}</td>
              <td>{item.allottee.mobileNumber}</td>
              <td>
                {item.asset.locationDetails &&
                item.asset.locationDetails.locality
                  ? getNameById(
                      self.state.locality,
                      item.asset.locationDetails.locality
                    )
                  : ""}
              </td>
              <td>{item.asset.assetCategory.id ? category_name : "-"}</td>
              <td>{item.asset.code}</td>
              <td>{item.createdDate}</td>
              <td>{item.action == "CREATE" ? "NEW" : item.action} </td>
              <td>{item.status} </td>
              <td>{item.source == "DATA_ENTRY" ? "Data Entry" : "System"}</td>
              <td>
                <div className="styled-select">
                  {getOption(
                    category_name == "Land" || category_name == "shop",
                    item
                  )}
                  
                </div>
              </td>
            </tr>
          );
        });
      }
    };

    /*const showCancelNEvict = function(status) {
          if(status == "APPROVED") {
            var values = [{label: "Cancel Agreement", value: "cancel"}, {label: "Evict Agreement", value: "eviction"}];
            return values.map(function(val, ind) {
              return (
                <option value={val.value}>{val.label}</option>
              )
            })
          }  else {
            return;
          }
        }*/

    const getDemandListing = function(agreement) {
      if (
        agreement.action === "CREATE" &&
        agreement.source === "DATA_ENTRY" &&
        agreement.status === "ACTIVE"
      ) {
        return <option value="addeditdemand">Add / Edit Demand </option>
      }
    };
    const getNoticeOption = function(agreement) {
      if (agreement.action === "CREATE" && agreement.status === "ACTIVE") {
        if (agreement.source === "DATA_ENTRY") {
          return <option value="generateNotice">Allotment Notice </option>;
        } else if (agreement.source === "SYSTEM") {
          return <option value="showNotice">Allotment Notice </option>
        }
      }
    };

    const getOption = function(isShopOrLand, item) {
       if (item.status === "INACTIVE" || item.status === "HISTORY") {
          return (
              <select
                  id="myOptions"
                  onChange={e => {
                      handleSelectChange(
                          e.target.value,
                          item.asset.id,
                          item.agreementNumber,
                          item.action,
                          item.id,
                          getValueByName("name", item.asset.assetCategory.id),
                          item.acknowledgementNumber,
                          item.status
                      );
                  }}
              >
                  <option value="">Select Action</option>
                  <option value="view">View</option>
              </select>
          );
      } else if (isShopOrLand) {
        return (
          <select
            id="myOptions"
            onChange={e => {
              handleSelectChange(
                e.target.value,
                item.asset.id,
                item.agreementNumber,
                  item.action,
                  item.id,
                  getValueByName("name", item.asset.assetCategory.id),
                item.acknowledgementNumber,
                item.status
              );
            }}
          >
            <option value="">Select Action</option>
            <option value="view">View</option>
            {/*<option value="renew">Renew</option>*/}
            {showCollectTaxOption(item)}
            {getDemandListing(item)}
            {getNoticeOption(item)}
          </select>
        );
      } else {
        return (
          <select
            id="myOptions"
            onChange={e => {
              handleSelectChange(
                e.target.value,
                item.asset.id,
                item.agreementNumber,
                  item.action,
                  item.id,
                  getValueByName("name", item.asset.assetCategory.id),
                item.acknowledgementNumber,
                item.status
              );
            }}
          >
            <option value="">Select Action</option>
            <option value="view">View</option>
            {showCollectTaxOption(item)}
            {getDemandListing(item)}
            {getNoticeOption(item)}
          </select>
        );
      }
    };
    const disbaled = function(type) {
      if (type === "view") {
        return "true";
      } else {
        return "false";
      }
    };
    return (
      <div>
        <div className="form-section">
          <h3>Search Agreement </h3>
          <div className="form-section-inner">
            <form
              onSubmit={e => {
                search(e);
              }}
            >
              <div className="">
                <div className="form-section">
                  <div className="row">
                    <div className="col-sm-3 col-sm-offset-5">
                      <label for="asset_category">
                          Asset category
                        <span> *</span>
                      </label>
                      <div className="styled-select">
                        <select
                          id="asset_category"
                          name="asset_category"
                          required="true"
                          value={assetCategory}
                          onChange={e => {
                            handleChange(e, "assetCategory");
                          }}
                        >
                          <option value="">Select Asset Category</option>
                          {renderOption(this.state.assetCategories)}
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-section">
                  <center><h5 style={{color : "red"}} >  Note : To view all transactions happened on Agreement, kindly select on History Search.</h5></center>
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 col-sm-offset-3">
                        <label
                          for="advanced_search"
                          style={{ marginBottom: "19px" }}
                        >
                          <input
                            type="checkbox"
                            name="advanced_search"
                            id="advanced_search"
                            value={advancedSearch}
                            checked={advancedSearch}
                            onChange={e => {
                              handleChange(e, "advancedSearch");
                            }}
                          />
                          <div
                            style={{
                              marginLeft: "10px",

                              display: "inline"
                            }}
                          >
                            History Search
                          </div>
                        </label>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label
                          for="agreement_number"
                          id="agreement_number_label"
                        >
                          Agreement Number
                        </label>
                      </div>
                      <div className="col-sm-6">
                        <input
                          type="text"
                          name="agreement_number"
                          id="agreement_number"
                          value={agreementNumber}
                          required={false}
                          onChange={e => {
                            handleChange(e, "agreementNumber");
                          }}
                        />
                      </div>
                    </div>
                  </div>

                  <div className="col-sm-6">
                    <div className="row" id="shop_number_field">
                      <div className="col-sm-6 label-text">
                        <label for="referenceNumber">Shop Number </label>
                      </div>
                      <div className="col-sm-6">
                        <input
                          type="text"
                          name="referenceNumber"
                          id="referenceNumber"
                          value={referenceNumber}
                          onChange={e => {
                            handleChange(e, "referenceNumber");
                          }}
                        />
                      </div>
                    </div>
                  </div>

                  <div className="col-sm-6">
                    <div className="row" id="old_agreement_number_field">
                      <div className="col-sm-6 label-text">
                        <label for="oldAgreementNumber">
                          Old Agreement Number{" "}
                        </label>
                      </div>
                      <div className="col-sm-6">
                        <input
                          type="text"
                          name="oldAgreementNumber"
                          id="oldAgreementNumber"
                          value={oldAgreementNumber}
                          onChange={e => {
                            handleChange(e, "oldAgreementNumber");
                          }}
                        />
                      </div>
                    </div>
                  </div>
                  <div id="collapse_search_fields">
                    <button
                      type="button"
                      className="btn btn-default btn-action pull-right"
                      style={{ marginRight: "2%" }}
                      data-toggle="collapse"
                      data-target="#demo"
                    >
                      <span className="glyphicon glyphicon-plus" />
                    </button>
                  </div>
                </div>
                <div id="demo" className="collapse">
                  <div className="row">
                    <br />
                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="contact_no">Allottee contact </label>
                        </div>
                        <div className="col-sm-6">
                          <div className="text-no-ui">
                            <span>+91</span>
                            <input
                              type="text"
                              id="contact_no"
                              onInput={e => {
                                handleMobileValidation(e);
                              }}
                              onInvalid={e => {
                                handleMobileValidation(e);
                              }}
                              name="contact_no"
                              value={mobileNumber}
                              maxLength="10"
                              onChange={e => {
                                handleChange(e, "mobileNumber");
                              }}
                            />
                          </div>
                        </div>
                      </div>
                    </div>

                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="name">Allottee Name </label>
                        </div>
                        <div className="col-sm-6">
                          <input
                            type="text"
                            id="name"
                            name="name"
                            value={allotteeName}
                            onChange={e => {
                              handleChange(e, "allotteeName");
                            }}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="locality">Locality</label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select
                              id="locality"
                              name="locality"
                              value={locality}
                              onChange={e => {
                                handleChange(e, "locality");
                              }}
                            >
                              <option value="">Choose locality</option>
                              {renderOption(this.state.locality)}
                            </select>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="ward">Revenue Ward no</label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select
                              id="ward"
                              name="ward"
                              value={revenueWard}
                              onChange={e => {
                                handleChange(e, "revenueWard");
                              }}
                            >
                              <option value="">Choose Revenue Wards</option>
                              {renderOption(this.state.revenueWards)}
                            </select>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="electionWard">Election Ward no </label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select
                              id="electionWard"
                              name="electionWard"
                              value={electionWard}
                              onChange={e => {
                                handleChange(e, "electionWard");
                              }}
                            >
                              <option value="">Choose Election Wards</option>
                              {renderOption(this.state.electionwards)}
                            </select>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="code">Asset Code </label>
                        </div>
                        <div className="col-sm-6">
                          <div className="search-ui">
                            <input
                              type="text"
                              name="code"
                              id="code"
                              pattern="[A-Za-z0-9]*"
                              value={code}
                              onChange={e => {
                                handleChange(e, "assetCode");
                              }}
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="fromDate">Agreement Created from </label>
                        </div>
                        <div className="col-sm-6">
                          <div className="text-no-ui">
                            <span className="glyphicon glyphicon-calendar" />
                            <input
                              className="date-picker"
                              type="text"
                              name="fromDate"
                              id="fromDate"
                              value={fromDate}
                              onChange={e => {
                                handleChange(e, "fromDate");
                              }}
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="toDate">Agreement Created To </label>
                        </div>
                        <div className="col-sm-6">
                          <div className="text-no-ui">
                            <span className="glyphicon glyphicon-calendar" />
                            <input
                              className="date-picker"
                              type="text"
                              name="toDate"
                              id="toDate"
                              value={toDate}
                              onChange={e => {
                                handleChange(e, "toDate");
                              }}
                            />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Acknowledgement Number </label>
                        </div>
                        <div className="col-sm-6">
                          <input
                            type="text"
                            name="acknowledgementNumber"
                            id="acknowledgementNumber"
                            value={acknowledgementNumber}
                            onChange={e => {
                              handleChange(e, "acknowledgementNumber");
                            }}
                          />
                        </div>
                      </div>
                    </div>
                    <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="shoppingComplexName">
                            Shopping Complex Name{" "}
                          </label>
                        </div>
                        <div className="col-sm-6">
                          <input
                            type="text"
                            name="shoppingComplexName"
                            id="shoppingComplexName"
                            value={shoppingComplexName}
                            onChange={e => {
                              handleChange(e, "shoppingComplexName");
                            }}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="text-center">
                <button type="submit" className="btn btn-submit">
                  Search
                </button>
                &nbsp;&nbsp;
                <button
                  type="button"
                  className="btn btn-close"
                  onClick={e => {
                    this.close();
                  }}
                >
                  Close
                </button>
              </div>
            </form>
          </div>
        </div>
        <div className="table-cont" id="table">
          {showTable()}
        </div>
        <div id="myModal" ref="modal" className="modal fade" role="dialog">
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal">
                  &times;
                </button>
                <h4 className="modal-title">Notice</h4>
              </div>
              <div className="modal-body">
                <iframe
                  title="Document"
                  src={this.state.iframe_src}
                  frameBorder="0"
                  allowFullScreen
                  height="500"
                  width="100%"
                />
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-default"
                  data-dismiss="modal"
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

ReactDOM.render(<AgreementSearch />, document.getElementById("root"));
