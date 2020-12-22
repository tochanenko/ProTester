import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {TestCasesRoutingModule} from './test-cases-routing.module';
import {ListComponent} from './list/list.component';
import {CreateComponent} from './create/create.component';
import {EditComponent} from './edit/edit.component';
import {MaterialModule} from '../../../../../services/material.module';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FlexLayoutModule} from '@angular/flex-layout';
import { ValidationComponent } from './validation/validation.component';
import {RunComponent} from './run/run.component';
import {SelectEnvComponent} from './select-env/select-env.component';

@NgModule({
  declarations: [ListComponent, CreateComponent, EditComponent, RunComponent, SelectEnvComponent, ValidationComponent],
  imports: [
    CommonModule,
    TestCasesRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule
  ]
})
export class TestCasesModule { }
