import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProjectsMenuRoutingModule } from './projects-menu-routing.module';
import { ViewComponent } from './view/view.component';
import {MaterialModule} from "../services/material.module";


@NgModule({
  declarations: [ViewComponent],
  imports: [
    CommonModule,
    ProjectsMenuRoutingModule,
    MaterialModule
  ]
})
export class ProjectsMenuModule { }
