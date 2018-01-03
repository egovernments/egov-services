var dat = {
  'swm.search' : {
     numCols: 4,
      useTimestamp: true,
      objectName: 'vendorPaymentDetails',
      url: '/swm-services/vendorpaymentdetails/_search',
      groups: [
        {
          name:'vendorPaymentSearch',
          label: 'swm.vendorpayment.search.vendorPaymentSearch',
          fields: [
            {
              name: ' contractno',
              jsonPath: 'contractNo',
              label: 'swm.vendorpayment.create.contractno',
              pattern: '',
              type: 'text',
              isRequired: false,
              isDisabled: false,
              defaultValue: '',
              url: '',
            },
            {
              name: 'paymentNo',
              jsonPath: 'paymentNos',
              label: 'swm.vendorpayment.create.paymentNo',
              pattern: '',
              type: 'text',
              isRequired: false,
              isDisabled: false,
              defaultValue: '',
              patternErrorMsg: '',
              url: '',
            },
            {
              name: 'dateRange',
              jsonPath: 'fromDate',
              label: 'FromDate',
              pattern: '',
              type: 'datePicker',
              isRequired: false,
              isDisabled: false,
              defaultValue: '',
              patternErrorMsg: '',
              url: '',
            },
            {
              name: 'dateRange',
              jsonPath: 'toDate',
              label: 'ToDate',
              pattern: '',
              type: 'datePicker',
              isRequired: false,
              isDisabled: false,
              defaultValue: '',
              patternErrorMsg: '',
              url: '',
            },
            {
              name: 'invoiceNo',
              jsonPath: 'invoiceNo',
              label: 'swm.vendorpayment.create.invoiceNo',
              pattern: '',
              type: 'text',
              isRequired: false,
              isDisabled: false,
              defaultValue: '',
              patternErrorMsg: '',
              url: '',
            }
          ]
        }
      ],
      result: {
        header: [
          {
            label: 'swm.vendorpayment.create.paymentNo',
          },
          {
            label: 'swm.vendorpayment.create.vendorName',
          },
          {
            label: 'swm.vendorpayment.search.vendorContractNo',
          },
          {
            label:  'swm.vendorpayment.create.vendorInvoiceAmount',
          }
        ],
        values: [
          'paymentNo',
          'vendorContract.vendor.name',
          'vendorContract.contractNo',
          'vendorInvoiceAmount',
        ],
        resultPath: 'vendorPaymentDetails',
        rowClickUrlUpdate: '/update/swm/vendorpaymentdetails/{paymentNo}',
        rowClickUrlView: '/view/swm/vendorpaymentdetails/{paymentNo}',
      }
    },
  'swm.create': {
    numCols: 4,
    useTimestamp: true,
    objectName: 'vendorPaymentDetails',
    title: 'swm.vendorpayment.create.title',
    groups: [
      {
        name: 'vendorPayment',
        label: '',
        fields: [
          {
            name: 'vendorName',
            jsonPath: 'vendorNo',
            label: 'swm.vendorpayment.create.vendorName',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url: 'swm-services/vendors/_search?|$.vendors.*.vendorNo|$.vendors.*.name',
            depedants: [
              {
                jsonPath: 'vendorPaymentDetails[0].vendorContract.contractNo',
                type: 'dropDown',
                pattern:
                  "swm-services/vendorcontracts/_search?&vendorNo={vendorNo}|$.vendorContracts.*.contractNo|$.vendorContracts.*.contractNo",
              }
            ]
          },
          {
            name: 'contractno',
            jsonPath: 'vendorPaymentDetails[0].vendorContract.contractNo',
            label: 'swm.vendorpayment.create.contractno',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ''
           // url: 'swm-services/vendorcontracts/_search?|$.vendorContracts.*.contractNo|$.vendorContracts.*.contractNo',
          },
          {
            name: 'approvalAmmount',
            jsonPath: 'vendorPaymentDetails[0].approvalAmmount',
            label: 'swm.vendorpayment.create.approvalAmmount',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'vendorInvoiceAmount',
            jsonPath: 'vendorPaymentDetails[0].vendorInvoiceAmount',
            label: 'swm.vendorpayment.create.vendorInvoiceAmount',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          }
        ],
      },
      {
        name: 'invoiceDetails',
        label: 'swm.vendorpayment.create.group.title.invoiceDetails',
        fields: [
          {
            name: 'invoiceNo',
            jsonPath: 'vendorPaymentDetails[0].invoiceNo',
            label: 'swm.vendorpayment.create.invoiceNo',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          },
           {
            name: 'fromDate',
            jsonPath: 'vendorPaymentDetails[0].fromDate',
            label: 'swm.vendorpayment.create.fromDate',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          },
           {
            name: 'toDate',
            jsonPath: 'vendorPaymentDetails[0].toDate',
            label: 'swm.vendorpayment.create.toDate',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          }
        ],
      },
      {
        name: 'documentsUpload',
        label: 'swm.vendorpayment.create.group.title.documentsUpload',
        fields: [
          {
            name: 'billInvoice',
            jsonPath: 'vendorPaymentDetails[0].documents[0].fileStoreId',
            label: 'swm.billInvoice.create.billInvoice',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ''
          },
          {
            name: 'document1',
            jsonPath: 'vendorPaymentDetails[0].documents[1].fileStoreId',
            label: 'swm.vendorpayment.create.document1',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ''
          },
          {
            name: 'document2',
            jsonPath: 'vendorPaymentDetails[0].documents[2].fileStoreId',
            label: 'swm.vendorpayment.create.document1',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ''
          }
        ]
      },
    ],
    url: '/swm-services/vendorpaymentdetails/_create',
    tenantIdRequired: true,
  },
  'swm.update': {
    numCols: 4,
    useTimestamp: true,
    objectName: 'vendorPaymentDetails',
    title: 'swm.vendorpayment.create.title',
    groups: [
      {
        name: 'vendorPayment',
        label: '',
        fields: [
          {
            name: 'vendorName',
            jsonPath: 'vendorPaymentDetails[0].vendorContract.vendors[0].vendorNo',
            label: 'swm.vendorpayment.create.vendorName',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url: 'swm-services/vendors/_search?|$.vendors.*.vendorNo|$.vendors.*.name',
            depedants: [
              {
                jsonPath: 'vendorPaymentDetails[0].vendorContract.contractNo',
                type: 'dropDown',
                pattern:
                  "swm-services/vendorcontracts/_search?&vendorNo={vendorPaymentDetails[0].vendorContract.vendors[0].vendorNo}|$.vendorContracts.*.contractNo|$.vendorContracts.*.contractNo",
              }
            ]
          },
          {
            name: 'contractno',
            jsonPath: 'vendorPaymentDetails[0].vendorContract.contractNo',
            label: 'swm.vendorpayment.create.contractno',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'approvalAmmount',
            jsonPath: 'vendorPaymentDetails[0].vendorContract.paymentAmount',
            label: 'swm.vendorpayment.create.approvalAmmount',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'vendorInvoiceAmount',
            jsonPath: 'vendorPaymentDetails[0].vendorInvoiceAmount',
            label: 'swm.vendorpayment.create.vendorInvoiceAmount',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          },
        ],
      },
      {
        name: 'invoiceDetails',
        label: 'swm.vendorpayment.create.group.title.invoiceDetails',
      fields: [
          {
            name: 'invoiceNo',
            jsonPath: 'vendorPaymentDetails[0].invoiceNo',
            label: 'swm.vendorpayment.create.invoiceNo',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          },
           {
            name: 'fromDate',
            jsonPath: 'vendorPaymentDetails[0].fromDate',
            label: 'swm.vendorpayment.create.fromDate',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          },
           {
            name: 'toDate',
            jsonPath: 'vendorPaymentDetails[0].toDate',
            label: 'swm.vendorpayment.create.toDate',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          }
        ]

      },
      {
        name: 'documentsUpload',
        label: 'swm.vendorpayment.create.group.title.documentsUpload',
        fields: [
          {
            name: 'billInvoice',
            jsonPath: 'vendorPaymentDetails[0].documents[0].fileStoreId',
            label: 'swm.billInvoice.create.billInvoice',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            readonly: true,
          },
          {
            name: 'document1',
            jsonPath: 'vendorPaymentDetails[0].documents[1].fileStoreId',
            label: 'swm.vendorpayment.create.document1',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            readonly: true,
          },
          {
            name: 'document2',
            jsonPath: 'vendorPaymentDetails[0].documents[2].fileStoreId',
            label: 'swm.vendorpayment.create.document1',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            readonly: true,
          },
        ]
      },
    ],

    url: '/swm-services/vendorpaymentdetails/_update',
    tenantIdRequired: true,
    searchUrl: '/swm-services/vendorpaymentdetails/_search?paymentNo={paymentNo}',
  },
 'swm.view': {
    numCols: 4,
    useTimestamp: true,
    objectName: 'vendorPaymentDetails',
    title: 'swm.vendorpayment.create.title',
    groups: [
      {
        name: 'vendorPayment',
        label: '',
        fields: [
          {
            name: 'vendorName',
            jsonPath: 'vendorPaymentDetails[0].vendorContract.vendors.name',
            label: 'swm.vendorpayment.create.vendorName',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'contractno',
            jsonPath: 'vendorPaymentDetails[0].vendorContract.contractNo',
            label: 'swm.vendorpayment.create.contractno',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'approvalAmmount',
            jsonPath: 'vendorPaymentDetails[0].vendorContract.paymentAmount',
            label: 'swm.vendorpayment.create.approvalAmmount',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'vendorInvoiceAmount',
            jsonPath: 'vendorPaymentDetails[0].vendorInvoiceAmount',
            label: 'swm.vendorpayment.create.vendorInvoiceAmount',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          }
        ]
      },
      {
        name: 'invoiceDetails',
        label: 'swm.vendorpayment.create.group.title.invoiceDetails',
      fields: [
          {
            name: 'invoiceNo',
            jsonPath: 'vendorPaymentDetails[0].invoiceNo',
            label: 'swm.vendorpayment.create.invoiceNo',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          },
           {
            name: 'fromDate',
            jsonPath: 'vendorPaymentDetails[0].fromDate',
            label: 'swm.vendorpayment.create.fromDate',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          },
           {
            name: 'toDate',
            jsonPath: 'vendorPaymentDetails[0].toDate',
            label: 'swm.vendorpayment.create.toDate',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'',
          }

        ]

      },
      {
        name: 'documentsUpload',
        label: 'swm.vendorpayment.create.group.title.documentsUpload',
        fields: [
          {
            name: 'billInvoice',
            jsonPath: 'vendorPaymentDetails[0].documents[0].fileStoreId',
            label: 'swm.billInvoice.create.billInvoice',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            readonly: true,
          },
          {
            name: 'document1',
            jsonPath: 'vendorPaymentDetails[0].documents[1].fileStoreId',
            label: 'swm.vendorpayment.create.document1',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            readonly: true,
          },
          {
            name: 'document2',
            jsonPath: 'vendorPaymentDetails[0].documents[2].fileStoreId',
            label: 'swm.vendorpayment.create.document1',
            type: 'singleFileUpload',
            pattern: '',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            readonly: true,
          },
        ]
      },
    ],
    tenantIdRequired: true,
    url: '/swm-services/vendorpaymentdetails/_search?paymentNo={paymentNo}',
  },
};
export default dat;