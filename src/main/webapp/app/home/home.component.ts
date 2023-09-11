/* eslint-disable @typescript-eslint/member-ordering */
/* eslint-disable no-console */
/* eslint-disable @typescript-eslint/no-inferrable-types */
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { INotification } from 'app/entities/notification/notification.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;

  private notification: INotification | undefined;
  plateNumber: string = "";
  appuser: IAppuser | undefined;
  
  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router) {}

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
    console.log(this.plateNumber)
    this.accountService.searchByplateNumber(this.plateNumber).subscribe((data: any) => {
      if (data) {
        this.appuser = data[0] as IAppuser;
      }
    });
  }

  public create (): void{
    console.log("Clicked");
    console.log(this.notification);
  }
}
