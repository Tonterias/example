import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotification, NewNotification } from '../notification.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotification for edit and NewNotificationFormGroupInput for create.
 */
type NotificationFormGroupInput = INotification | PartialWithRequiredKeyOf<NewNotification>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotification | NewNotification> = Omit<T, 'date'> & {
  date?: string | null;
};

type NotificationFormRawValue = FormValueOf<INotification>;

type NewNotificationFormRawValue = FormValueOf<NewNotification>;

type NotificationFormDefaults = Pick<NewNotification, 'id' | 'date'>;

type NotificationFormGroupContent = {
  id: FormControl<NotificationFormRawValue['id'] | NewNotification['id']>;
  date: FormControl<NotificationFormRawValue['date']>;
  message: FormControl<NotificationFormRawValue['message']>;
  status: FormControl<NotificationFormRawValue['status']>;
  appuser: FormControl<NotificationFormRawValue['appuser']>;
};

export type NotificationFormGroup = FormGroup<NotificationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificationFormService {
  createNotificationFormGroup(notification: NotificationFormGroupInput = { id: null }): NotificationFormGroup {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({
      ...this.getFormDefaults(),
      ...notification,
    });
    return new FormGroup<NotificationFormGroupContent>({
      id: new FormControl(
        { value: notificationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(notificationRawValue.date),
      message: new FormControl(notificationRawValue.message, {
        validators: [Validators.required],
      }),
      status: new FormControl(notificationRawValue.status),
      appuser: new FormControl(notificationRawValue.appuser, {
        validators: [Validators.required],
      }),
    });
  }

  getNotification(form: NotificationFormGroup): INotification | NewNotification {
    return this.convertNotificationRawValueToNotification(form.getRawValue() as NotificationFormRawValue | NewNotificationFormRawValue);
  }

  resetForm(form: NotificationFormGroup, notification: NotificationFormGroupInput): void {
    const notificationRawValue = this.convertNotificationToNotificationRawValue({ ...this.getFormDefaults(), ...notification });
    form.reset(
      {
        ...notificationRawValue,
        id: { value: notificationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): NotificationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertNotificationRawValueToNotification(
    rawNotification: NotificationFormRawValue | NewNotificationFormRawValue
  ): INotification | NewNotification {
    return {
      ...rawNotification,
      date: dayjs(rawNotification.date, DATE_TIME_FORMAT),
    };
  }

  private convertNotificationToNotificationRawValue(
    notification: INotification | (Partial<NewNotification> & NotificationFormDefaults)
  ): NotificationFormRawValue | PartialWithRequiredKeyOf<NewNotificationFormRawValue> {
    return {
      ...notification,
      date: notification.date ? notification.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
