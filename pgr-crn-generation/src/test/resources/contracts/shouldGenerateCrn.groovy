package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method 'GET'
        url '/crn'
        headers {
            contentType('application/json')
        }
    }

    response {
        status 200
        body([
               value: '00056-2017-PD'
        ])
        headers {
            contentType('application/json')
        }
    }
}