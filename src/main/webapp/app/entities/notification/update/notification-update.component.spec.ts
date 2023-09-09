import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NotificationFormService } from './notification-form.service';
import { NotificationService } from '../service/notification.service';
import { INotification } from '../notification.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

import { NotificationUpdateComponent } from './notification-update.component';

describe('Notification Management Update Component', () => {
  let comp: NotificationUpdateComponent;
  let fixture: ComponentFixture<NotificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificationFormService: NotificationFormService;
  let notificationService: NotificationService;
  let appuserService: AppuserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NotificationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(NotificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificationFormService = TestBed.inject(NotificationFormService);
    notificationService = TestBed.inject(NotificationService);
    appuserService = TestBed.inject(AppuserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Appuser query and add missing value', () => {
      const notification: INotification = { id: 456 };
      const appuser: IAppuser = { id: 39395 };
      notification.appuser = appuser;

      const appuserCollection: IAppuser[] = [{ id: 28973 }];
      jest.spyOn(appuserService, 'query').mockReturnValue(of(new HttpResponse({ body: appuserCollection })));
      const additionalAppusers = [appuser];
      const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
      jest.spyOn(appuserService, 'addAppuserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      expect(appuserService.query).toHaveBeenCalled();
      expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(
        appuserCollection,
        ...additionalAppusers.map(expect.objectContaining)
      );
      expect(comp.appusersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const notification: INotification = { id: 456 };
      const appuser: IAppuser = { id: 62957 };
      notification.appuser = appuser;

      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      expect(comp.appusersSharedCollection).toContain(appuser);
      expect(comp.notification).toEqual(notification);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 123 };
      jest.spyOn(notificationFormService, 'getNotification').mockReturnValue(notification);
      jest.spyOn(notificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notification }));
      saveSubject.complete();

      // THEN
      expect(notificationFormService.getNotification).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificationService.update).toHaveBeenCalledWith(expect.objectContaining(notification));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 123 };
      jest.spyOn(notificationFormService, 'getNotification').mockReturnValue({ id: null });
      jest.spyOn(notificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notification }));
      saveSubject.complete();

      // THEN
      expect(notificationFormService.getNotification).toHaveBeenCalled();
      expect(notificationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotification>>();
      const notification = { id: 123 };
      jest.spyOn(notificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppuser', () => {
      it('Should forward to appuserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appuserService, 'compareAppuser');
        comp.compareAppuser(entity, entity2);
        expect(appuserService.compareAppuser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
