Feature: In this feature we are going to test DocumentType Master

  Scenario: Create Search And Update DocumentType

    ### On Login Screen ###
    Given user on Login screen verifies signInText has visible value Sign In
    And user on Login screen types on username value elzan
    And user on Login screen types on password value 12345678
    And user on Login screen clicks on signIn

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Create Document Type
    And user on Home screen clicks on secondMenuItem

    ### On Create Document Type Screen ###
    And user on TLDocumentTypeMaster screen will see the applicationType
    And user on TLDocumentTypeMaster screen selects on applicationType value NEW
    And user on TLDocumentTypeMaster screen selects on category value Advertisement
    And user on TLDocumentTypeMaster screen selects on subCategory value Paste Ad
    And user on TLDocumentTypeMaster screen types on name value "DocumentType ",4 random characters
    And user on TLDocumentTypeMaster screen copies the name to documentTypeName
    And user on TLDocumentTypeMaster screen forceclicks on enabled
    And user on TLDocumentTypeMaster screen clicks on text value Create

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View Document Type
    And user on Home screen clicks on secondMenuItem

    ### On View Document Type Screen ###
    And user on TLDocumentTypeMaster screen verifies text has visible value Search Document Type
    And user on TLDocumentTypeMaster screen types on searchDocumentName value documentTypeName
    And user on TLDocumentTypeMaster screen clicks on text value Search
    And user on TLDocumentTypeMaster screen verifies text has visible value documentTypeName

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value Modify Document Type
    And user on Home screen clicks on firstMenuItem

    ### On View Document Type Screen ###
    And user on TLDocumentTypeMaster screen verifies text has visible value Search Document Type
    And user on TLDocumentTypeMaster screen types on searchDocumentName value documentTypeName
    And user on TLDocumentTypeMaster screen clicks on text value Search
    And user on TLDocumentTypeMaster screen verifies text has visible value documentTypeName
    And user on TLDocumentTypeMaster screen clicks on text value documentTypeName

    And user on TLDocumentTypeMaster screen verifies text has visible value Trade license Document type Modify
    And user on TLDocumentTypeMaster screen types on updateDocumentName value "UpdateDocumentType ",4 random characters
    And user on TLDocumentTypeMaster screen copies the updateDocumentName to documentTypeName
    And user on TLDocumentTypeMaster screen clicks on text value Update

    ### On Homepage Screen ###
    And user on Home screen will wait until the page loads
    And user on Home screen will see the menu
    And user on Home screen clicks on menu
    And user on Home screen types on menuSearch value View Document Type
    And user on Home screen clicks on secondMenuItem

    ### On View Document Type Screen ###
    And user on TLDocumentTypeMaster screen verifies text has visible value Search Document Type
    And user on TLDocumentTypeMaster screen types on searchDocumentName value documentTypeName
    And user on TLDocumentTypeMaster screen clicks on text value Search
    And user on TLDocumentTypeMaster screen verifies text has visible value documentTypeName

    ### Logout ###
    And Intent:LogoutIntentTest



