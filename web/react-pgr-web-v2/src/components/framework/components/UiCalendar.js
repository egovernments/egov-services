import React, { Component } from 'react';
import { translate } from '../../common/common';
import BigCalendar from 'react-big-calendar';
import moment from 'moment';
import { connect } from 'react-redux';
import _ from 'lodash';

BigCalendar.momentLocalizer(moment);

export default class UiCalendar extends Component {
  constructor(props) {
    super(props);
  }

  renderCalendar = item => {
    let allViews = Object.keys(BigCalendar.Views).map(k => {
      return BigCalendar.Views[k];
    });
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
                  event: this.EventAgenda,
                },
              }}
            />
          </div>
        );
    }
  };

  Event({ event }) {
    return (
      <span>
        <strong>{event.title}</strong>
        {event.desc && ':  ' + event.desc}
      </span>
    );
  }

  EventAgenda({ event }) {
    return (
      <span>
        <em style={{ color: 'blue' }}>{event.title}</em>
        <p>{event.desc}</p>
      </span>
    );
  }

  getValue(item) {
    var eventsObj = [];
    var edata = this.props.getVal(item);
    if (edata && edata.length > 0) {
      edata.map((v, i) => {
        var hearingTimeDate = new Date();
        if (v.nextHearingDate && v.nextHearingTime) {
          var timeDate = new Date(parseInt(v.nextHearingTime));
          var dataDate = new Date(v.nextHearingDate);
          hearingTimeDate = timeDate.setDate(dataDate.getDate());
        } else {
          hearingTimeDate = v.nextHearingDate ? v.nextHearingDate : new Date();
        }
        eventsObj.push({
          title: 'Case' + v.caseNo,
          start: new Date(hearingTimeDate),
          end: new Date(hearingTimeDate),
          desc: v.departmentConcernPerson,
        });
      });
    }

    return eventsObj;
  }

  render() {
    return this.renderCalendar(this.props.item);
  }
}
