import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'appuser',
        data: { pageTitle: 'exampleApp.appuser.home.title' },
        loadChildren: () => import('./appuser/appuser.module').then(m => m.AppuserModule),
      },
      {
        path: 'notification',
        data: { pageTitle: 'exampleApp.notification.home.title' },
        loadChildren: () => import('./notification/notification.module').then(m => m.NotificationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
