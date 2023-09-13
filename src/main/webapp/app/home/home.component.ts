import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { PlateSearchFormService, PlateSearchFromGroup } from './plate-search-form.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;

  plateNumber: string = "";
  private readonly destroy$ = new Subject<void>();

  plateExists = false;

  plateForm : PlateSearchFromGroup = this.plateNumberFormService.createPlateSearchFormGroup();
  
  constructor(
    private accountService: AccountService,
    private router: Router, 
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
      }
    })
  }
}
