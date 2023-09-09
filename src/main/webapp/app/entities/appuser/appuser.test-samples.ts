import dayjs from 'dayjs/esm';

import { IAppuser, NewAppuser } from './appuser.model';

export const sampleWithRequiredData: IAppuser = {
  id: 77401,
  date: dayjs('2023-09-08T16:49'),
  plateNumber: 'indexing Birr THX',
};

export const sampleWithPartialData: IAppuser = {
  id: 34478,
  date: dayjs('2023-09-09T04:06'),
  plateNumber: 'Re-engineered Games',
  email: 'Sebastian.Bosco13@gmail.com',
};

export const sampleWithFullData: IAppuser = {
  id: 97991,
  date: dayjs('2023-09-08T22:54'),
  plateNumber: 'application HDD',
  firstName: 'Micah',
  lastName: 'McCullough',
  email: 'Malika.Buckridge@hotmail.com',
};

export const sampleWithNewData: NewAppuser = {
  date: dayjs('2023-09-09T03:18'),
  plateNumber: 'pixel Buckinghamshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
