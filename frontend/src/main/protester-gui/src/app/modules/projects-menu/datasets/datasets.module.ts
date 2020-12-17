import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DatasetsRoutingModule } from './datasets-routing.module';
import { ListComponent } from './list/list.component';
import {MaterialModule} from "../../../services/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ViewComponent } from './view/view.component';
import { EditComponent } from './edit/edit.component';
import { DeleteComponent } from './delete/delete.component';
import { CreateComponent } from './create/create.component';


@NgModule({
  declarations: [ListComponent, ViewComponent, EditComponent, DeleteComponent, CreateComponent],
  imports: [
    CommonModule,
    DatasetsRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class DatasetsModule { }
