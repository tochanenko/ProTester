import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TestCasesRoutingModule } from './test-cases-routing.module';
import { ListComponent } from './list/list.component';
import { CreateComponent } from './create/create.component';
import { EditComponent } from './edit/edit.component';
import {MaterialModule} from "../../../../services/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [ListComponent, CreateComponent, EditComponent],
  imports: [
    CommonModule,
    TestCasesRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class TestCasesModule { }
