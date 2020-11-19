import {FormGroup} from '@angular/forms';

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
}
