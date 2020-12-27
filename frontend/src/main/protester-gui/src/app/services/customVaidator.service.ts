import {AbstractControl, FormGroup, ValidatorFn} from '@angular/forms';
import {Step} from "../models/step.model";

export class CustomValidator {
  static passwordMatchValidator(password: string, passwordConfirm: string): (formGroup: FormGroup) => void {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[password];
      const matchingControl = formGroup.controls[passwordConfirm];
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({mustMatch: true});
      }
    };
  }

  static placeholderValidator(formGroup: FormGroup): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      let items = "";
      let controls = formGroup.controls;
      const name_field = controls['name'];
      Object.keys(controls).forEach( key => {
        if (controls[key].value.includes("${") && !name_field.value.includes(controls[key].value) && !items.includes(controls[key].value)) {
          items += controls[key].value + " ";
        }
      })

      name_field.setErrors({needToAdd: items});
      return items ? {needToAdd: items} : null;
    };
  }

}
