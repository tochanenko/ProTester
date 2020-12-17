import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LibrariesRoutingModule } from './libraries-routing.module';
import { ViewComponent } from './view/view.component';
import { CreateComponent } from './create/create.component';
import { EditComponent } from './edit/edit.component';
import {MaterialModule} from "../../../services/material.module";
import {ReactiveFormsModule} from "@angular/forms";
import { ListComponent } from './list/list.component';


@NgModule({
  declarations: [ViewComponent, CreateComponent, EditComponent, ListComponent],
  imports: [
    CommonModule,
    LibrariesRoutingModule,
    MaterialModule,
    ReactiveFormsModule
  ]
})
export class LibrariesModule { }
