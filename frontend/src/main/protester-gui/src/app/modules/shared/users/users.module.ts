import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { ListComponent } from './list/list.component';
import {MaterialModule} from "../../../services/material.module";
import { ViewComponent } from './view/view.component';
import { EditComponent } from './edit/edit.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCheckboxModule} from "@angular/material/checkbox";


@NgModule({
  declarations: [ListComponent, ViewComponent, EditComponent],
  imports: [
    CommonModule,
    UsersRoutingModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    MatCheckboxModule
  ]
})
export class UsersModule { }
