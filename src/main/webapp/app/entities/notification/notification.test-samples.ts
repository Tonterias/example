import dayjs from 'dayjs/esm';

import { Message } from 'app/entities/enumerations/message.model';
import { Status } from 'app/entities/enumerations/status.model';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 30621,
  message: Message['MESSAGE1'],
};

export const sampleWithPartialData: INotification = {
  id: 55330,
  message: Message['MESSAGE4'],
  status: Status['NOTIFIED'],
};

export const sampleWithFullData: INotification = {
  id: 25492,
  date: dayjs('2023-09-09T10:25'),
  message: Message['MESSAGE3'],
  status: Status['INMACULATE'],
};

export const sampleWithNewData: NewNotification = {
  message: Message['MESSAGE1'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
