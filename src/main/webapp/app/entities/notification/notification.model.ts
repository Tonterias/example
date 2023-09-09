import dayjs from 'dayjs/esm';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { Message } from 'app/entities/enumerations/message.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface INotification {
  id: number;
  date?: dayjs.Dayjs | null;
  message?: Message | null;
  status?: Status | null;
  appuser?: Pick<IAppuser, 'id' | 'plateNumber'> | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
