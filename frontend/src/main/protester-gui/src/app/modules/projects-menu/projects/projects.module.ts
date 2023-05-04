import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProjectsRoutingModule } from './projects-routing.module';
import { ListComponent } from './list/list.component';
import {MaterialModule} from "../../../services/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { EditComponent } from './edit/edit.component';
import { CreateComponent } from './create/create.component';
import {ViewModule} from "./view/view.module";
import {TestCaseResultTableComponent} from "../test-case-result-table/test-case-result-table.component";


@NgModule({
  declarations: [ListComponent, EditComponent, CreateComponent, TestCaseResultTableComponent],
  imports: [
    CommonModule,
    ProjectsRoutingModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    ViewModule
  ]
})
export class ProjectsModule { }
