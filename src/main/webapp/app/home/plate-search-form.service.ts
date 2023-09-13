import { Injectable } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";

export type PlateSearchFromGroup = FormGroup<any>;

@Injectable({ providedIn: 'root' })
export class PlateSearchFormService {

    createPlateSearchFormGroup() {

        return new FormGroup({
            plateNumber: new FormControl('', [Validators.required]),
            message: new FormControl('', [Validators.required])
        })

    }
 
}