import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list/list.component';
import {MaterialModule} from '../../../../../services/material.module';
import {EnvironmentRoutingModule} from './environment-routing.module';
import { CreateComponent } from './create/create.component';
import { EditComponent } from './edit/edit.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';



@NgModule({
  declarations: [ListComponent, CreateComponent, EditComponent],
  imports: [
    CommonModule,
    MaterialModule,
    EnvironmentRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class EnvironmentModule { }
