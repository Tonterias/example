/* eslint-disable no-console */
/* eslint-disable @typescript-eslint/no-inferrable-types */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { map, takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { Message } from 'app/entities/enumerations/message.model';
import { Status } from 'app/entities/enumerations/status.model';


import dayjs from 'dayjs/esm';

import { PlateSearchFormService, PlateSearchFromGroup } from './plate-search-form.service';
import { NotificationService } from '../entities/notification/service/notification.service';
import { INotification, NewNotification } from '../entities/notification/notification.model';
import { IAppuser } from '../entities/appuser/appuser.model';
import { HttpResponse } from '@angular/common/http';


@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;

  plateNumber: string = "";
  appuser: IAppuser | undefined;
  
  private readonly destroy$ = new Subject<void>();

  plateExists = false;
  appUser: any = null;

  notification: INotification | null = null;

  plateForm : PlateSearchFromGroup = this.plateNumberFormService.createPlateSearchFormGroup();
  messageValues = Object.keys(Message);

  constructor(
    private accountService: AccountService,
    protected appuserService: AppuserService,
    private router: Router, 
    protected notificationService: NotificationService,
    protected plateNumberFormService: PlateSearchFormService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  searchByplateNumber(): void {
    console.log(this.plateForm)
    console.log("====> " + this.plateForm.get('plateNumber'))
    console.log("====> " + this.plateForm.value.plateNumber)
    this.accountService.searchByplateNumber(this.plateForm.value.plateNumber).subscribe((data: any) => {
      console.log(data)
      if(data.length == 0){
        this.plateExists = false;
      }else{
        this.plateExists = true;
        this.appUser = data[0];
      }
    })

  }

  sendNotification(): void {
    console.log("Send notification")
    const currentTime = dayjs();
    const notification: NewNotification = {
      id: null,
      date: currentTime,
      message: this.plateForm.value.message,
      status: Status['INMACULATE'],
      appuser: this.appUser
    }
    this.notificationService.create(notification).subscribe((data) => {
      console.log("Notification created from home page")
    }, (error) => {
      console.log("Error " + error)
    })
  }

}
