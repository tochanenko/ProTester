import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ActionsRoutingModule } from './actions-routing.module';
import { ListComponent } from './list/list.component';
import { EditComponent } from './edit/edit.component';
import {MaterialModule} from "../../../services/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [ListComponent, EditComponent],
  imports: [
    CommonModule,
    ActionsRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ActionsModule { }
