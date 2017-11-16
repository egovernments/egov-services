import React, { Component } from 'react';
import { translate } from '../../common/common';
import BigCalendar from 'react-big-calendar';
import moment from 'moment';
import { connect } from "react-redux";
import _ from "lodash";


BigCalendar.momentLocalizer(moment);

export default class UiCalendar extends Component {
    constructor(props) {
        super(props);
    }

    renderCalendar = (item) => {
        let allViews = Object.keys(BigCalendar.Views).map(k => {
            return BigCalendar.Views[k]
        })
        switch (this.props.ui) {
            case 'google':
                return (
                    <div>
                        <BigCalendar
                            events={this.getValue(item.jsonPath)}
                            views={allViews}
                            step={60}
                            defaultDate={new Date()}
                            components={{
                                event: this.Event,
                                agenda: {
                                    event: this.EventAgenda
                                }
                            }}
                        />
                    </div>
                );
        }
    }

    Event({ event }) {
        return (
            <span>
                <strong>
                    {event.title}
                </strong>
                {event.desc && (':  ' + event.desc)}
            </span>
        )
    }

    EventAgenda({ event }) {
        return <span>
            <em style={{ color: 'blue' }}>{event.title}</em>
            <p>{event.desc}</p>
        </span>
    }

    getValue(item) {
        var eventsObj = [];
        var edata = this.props.getVal(item);
        console.log(edata);
        if (edata && edata.length > 0) {
            edata.map((v, i) => {
                debugger;
                eventsObj.push({
                    'title': 'Case'+v.caseNo,
                    'start': new Date(v.nextHearingDate),
                    'end': new Date(v.nextHearingDate),
                    'desc': v.departmentConcernPerson
                }
                );
            }
            );
        }

        return eventsObj;
    }

    render() {
        return (
            this.renderCalendar(this.props.item)
        );
    }
}